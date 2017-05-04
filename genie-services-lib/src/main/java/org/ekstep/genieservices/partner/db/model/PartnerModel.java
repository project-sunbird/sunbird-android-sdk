package org.ekstep.genieservices.partner.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.exception.EncryptionException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.contract.PartnerEntry;
import org.ekstep.genieservices.util.Base64;
import org.ekstep.genieservices.util.Crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class PartnerModel implements IWritable, IReadable {
    public static final String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
    public static final String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
    public static final String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";
    private static final String TAG = "model-Partner";
    private static final String TEST = "test";
    private String partnerID;
    private String publicKey;
    private String publicKeyID;
    private ContentValues contentValues;
    private List<String> errorMessages;
    private String data;
    private String encryptedKey;
    private String iv;
    private AppContext appContext;
    private IDBSession idbSession;

    private PartnerModel(String partnerID, String publicKey, AppContext appContext, IDBSession idbSession) {
        this.partnerID = partnerID;
        this.appContext = appContext;
        this.idbSession = idbSession;
        this.publicKey = publicKey;
        contentValues = new ContentValues();
        errorMessages = new ArrayList<String>();
        if (this.publicKey != null) {
            try {
                this.publicKeyID = Crypto.checksum(this.publicKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                Logger.e(appContext, TAG, "Bad Algorithm", e);
            }
        }
    }

    public static PartnerModel findByPartnerId(IDBSession idbSession, AppContext appContext, String partnerID, String publicKey) {
        PartnerModel partnerModel = new PartnerModel(partnerID, publicKey, appContext, idbSession);
        idbSession.read(partnerModel);
        return partnerModel;
    }

    public Boolean isValid() {
        if (partnerID == null || partnerID.isEmpty())
            errorMessages.add(MISSING_PARTNER_ID);
        if (publicKey == null || publicKey.isEmpty())
            errorMessages.add(MISSING_PUBLIC_KEY);
        try {
//            Crypto.encryptWithPublic(TEST, publicKey);
            Crypto.checksum(this.publicKey);
        } catch (Exception e) {
            Logger.e(appContext, TAG, "ERROR Bad Public Key!", e);
            errorMessages.add(INVALID_RSA_PUBLIC_KEY);
        }
        Boolean result = errorMessages.isEmpty();
        Logger.i(appContext, TAG, "Valid? " + result);
        Logger.i(appContext, TAG, "Errors: " + errorMessages.toString());
        return result;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(PartnerEntry.COLUMN_NAME_UID, partnerID);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY, publicKey);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY_ID, publicKeyID);
        return contentValues;
    }

    @Override
    public void updateId(long id) {

    }

    public boolean exists() {
        return (partnerID != null && !partnerID.isEmpty());
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            partnerID = cursor.getString(1);
            publicKey = cursor.getString(2);
            publicKeyID = cursor.getString(3);
        } else
            partnerID = "";
        return this;
    }

    @Override
    public String getTableName() {
        return PartnerEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        Logger.i(appContext, TAG, String.format("SEARCH partnerID: %s", partnerID));
        String selectionCriteria = String.format(Locale.US, "where partnerID = '%s'", partnerID);
        return selectionCriteria;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public void save() {
        // TODO: 2/5/17 Generate GERegisterPartner
        idbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                dbSession.create(PartnerModel.this);
                return null;
            }
        });
    }

    public void startSession() {
        Logger.i(appContext, TAG, "SESSION START Partner: " + partnerID);
        PartnerSessionModel partnerSessionModel = PartnerSessionModel.build(appContext, partnerID);
        partnerSessionModel.save();
        // TODO: 2/5/17 Generate GEStartPartnerSession
    }


    public void terminateSession() {
        if (isSessionToTerminate(partnerID)) {
            Logger.i(appContext, TAG, "SESSION TERMINATE Partner: " + partnerID);
            PartnerSessionModel partnerSessionModel = PartnerSessionModel.build(appContext, partnerID);
            partnerSessionModel.clear();
        }
    }

    private Boolean isSessionToTerminate(String partnerId) {
        Set<String> activePartners = PartnerSessionModel.findActivePartners(appContext);
        return (activePartners != null && activePartners.contains(partnerId));
    }

    private String encryptDataWithPublic(byte[] data, String publicKey) throws EncryptionException {
        try {
            return Crypto.encryptWithPublic(data, publicKey);
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        }
    }

    private String encryptDataWithAES(String data, SecretKey AESKey, IvParameterSpec iv) throws EncryptionException {
        try {
            return Crypto.encryptWithAES(data, AESKey, iv);
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            Logger.e(appContext, TAG, "InvalidAlgorithmParameterException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        }
    }

    public void sendData() {
        Logger.i(appContext, TAG, "SEND DATA Partner: " + partnerID);
        // TODO: 2/5/17 Generate and save GESendPartnerData
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) throws EncryptionException {
        Logger.i(appContext, TAG, "--> ENCRYPTING");
        try {
            SecretKey AESKey = Crypto.generateAESKey(); //ok
            IvParameterSpec iv = Crypto.generateIVSpecForAES();
            String ivString = Base64.encodeToString(iv.getIV(), Base64.DEFAULT);
            PublicKey publicKey = Crypto.generatePublicKey(this.publicKey);
            byte[] encryptedKey = Crypto.encryptSecretKeyWithRSAPublic(AESKey, publicKey);
            String encryptedKeyString = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            String encryptedData = encryptDataWithAES(data, AESKey, iv);
            this.iv = ivString;
            Logger.i(appContext, TAG, "IV:" + ivString);
            this.data = encryptedData;
            this.encryptedKey = encryptedKeyString;
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        }
        Logger.i(appContext, TAG, "<-- ENCRYPTED");
    }

    public String getEncryptedKey() {
        return this.encryptedKey;
    }

    public String getIV() {
        return this.iv;
    }
}
