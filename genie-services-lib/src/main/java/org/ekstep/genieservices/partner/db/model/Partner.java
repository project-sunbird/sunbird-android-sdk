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
import org.ekstep.genieservices.util.Crypto;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Partner implements IWritable, IReadable {
    public static final String SHARED_PREF_KEY = "partnerid";
    public static final String SHARED_PREF_SESSION_KEY = "partnersessionid";
    public static final String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
    public static final String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
    public static final String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";
    private static final String TAG = "model-Partner";
    private static final String TEST = "test";
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String publicKey;
    private String publicKeyID;
    private ContentValues contentValues;
    private List<String> errorMessages;
    //    private transient SharedPreferences sharedPref;
//    private transient Context context;
    private String data;
    private String encryptedKey;
    private String iv;
    private AppContext appContext;

    private Partner(String gameID, String gameVersion, String partnerID, String publicKey, AppContext appContext) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.partnerID = partnerID;
        this.appContext = appContext;
        this.publicKey = publicKey;
//        this.sharedPref = this.context.getSharedPreferences(
//                Constants.SHARED_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
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

    public static Partner findByPartnerId(AppContext appContext, String partnerID, String publicKey) {
        Partner partner = new Partner(appContext.getGDataId(), appContext.getGDataVersionName(), partnerID, publicKey, appContext);
        appContext.getDBSession().read(partner);
        return partner;
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
        appContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                dbSession.create(Partner.this);
                return null;
            }
        });
    }

    public void initialize() {
        appContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                dbSession.read(Partner.this);
                return null;
            }
        });
    }

    public void startSession() {
        Logger.i(appContext, TAG, "SESSION START Partner: " + partnerID);
        saveSession();
        // TODO: 2/5/17 Generate GEStartPartnerSession
    }

    protected void saveSession() {
        Logger.i(appContext, TAG, String.format("UPDATE SharedPreferences; partnerID: %s", partnerID));
        String sid = Crypto.getUID();
        Set<String> partnerSet = appContext.getKeyValueStore().getStringSet(SHARED_PREF_KEY, null);
        if (partnerSet == null) {
            partnerSet = new HashSet<String>(Arrays.asList(partnerID));
        } else {
            partnerSet.add(partnerID);
        }
        appContext.getKeyValueStore().putStringSet(SHARED_PREF_KEY, partnerSet);
        appContext.getKeyValueStore().putLong("partnerSET", getEpoch());
        appContext.getKeyValueStore().putString(SHARED_PREF_SESSION_KEY, sid);
    }

    public void terminateSession() {
        if (isSessionToTerminate(partnerID)) {
            Logger.i(appContext, TAG, "SESSION TERMINATE Partner: " + partnerID);
            clearSession();
        }
    }

    private void clearSession() {
        Logger.i(appContext, TAG, String.format("CLEAR SharedPreferences; partnerID: %s", partnerID));

        Set<String> activePartners = appContext.getKeyValueStore().getStringSet(SHARED_PREF_KEY, null);
        activePartners.remove(partnerID);

        appContext.getKeyValueStore().putStringSet(SHARED_PREF_KEY, activePartners);
        appContext.getKeyValueStore().putLong("partnerSET", 0L);
        appContext.getKeyValueStore().putLong("partnerUNSET", getEpoch());
        appContext.getKeyValueStore().putString(SHARED_PREF_SESSION_KEY, "");
    }

    private Long getEpoch() {
        Date date = new Date();
        return date.getTime();
    }

    private Long sessionLength() {
        Long length = 0L;
        Long t0 = appContext.getKeyValueStore().getLong("partnerSET", 0L);
        if (t0 != 0L) {
            length = getEpoch() - t0;
        }
        return length;
    }

    private String getPartnerSID() {
        return appContext.getKeyValueStore().getString(SHARED_PREF_SESSION_KEY, "");
    }

    private Set<String> getActivePartners() {
        return appContext.getKeyValueStore().getStringSet(SHARED_PREF_KEY, null);
    }

    private Boolean isSessionToTerminate(String partnerId) {
        Set<String> activePartners = getActivePartners();
        return (activePartners != null && activePartners.contains(partnerId));
    }

    private String encryptDataWithPublic(byte[] data, String publicKey) throws EncryptionException {
//        try {
//            return Crypto.encryptWithPublic(data, publicKey);
//        } catch (InvalidKeyException e) {
//            Logger.e(appContext, TAG, "InvalidKeyException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidKeySpecException e) {
//            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (UnsupportedEncodingException e) {
//            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (BadPaddingException e) {
//            Logger.e(appContext, TAG, "BadPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (IllegalBlockSizeException e) {
//            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchAlgorithmException e) {
//            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchPaddingException e) {
//            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        }

        // TODO: 2/5/17 Has to return string after uncommenting above code
        return null;
    }

    private String encryptDataWithAES(String data, SecretKey AESKey, IvParameterSpec iv) throws EncryptionException {
//        try {
//            return Crypto.encryptWithAES(data, AESKey, iv);
//        } catch (InvalidKeyException e) {
//            Logger.e(TAG, "InvalidKeyException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (BadPaddingException e) {
//            Logger.e(TAG, "BadPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (IllegalBlockSizeException e) {
//            Logger.e(TAG, "IllegalBlockSizeException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchAlgorithmException e) {
//            Logger.e(TAG, "NoSuchAlgorithmException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchPaddingException e) {
//            Logger.e(TAG, "NoSuchPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidAlgorithmParameterException e) {
//            Logger.e(TAG, "InvalidAlgorithmParameterException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (UnsupportedEncodingException e) {
//            Logger.e(TAG, "UnsupportedEncodingException", e);
//            throw new EncryptionException(e.getMessage());
//        }

        // TODO: 2/5/17 Has to return string after uncommenting above code
        return null;
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
//        try {
//            SecretKey AESKey = Crypto.generateAESKey(); //ok
//            IvParameterSpec iv = Crypto.generateIVSpecForAES();
//            String ivString = Base64.encodeToString(iv.getIV(), Base64.DEFAULT);
//            PublicKey publicKey = Crypto.generatePublicKey(this.publicKey);
//            byte[] encryptedKey = Crypto.encryptSecretKeyWithRSAPublic(AESKey, publicKey);
//            String encryptedKeyString = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
//            String encryptedData = encryptDataWithAES(data, AESKey, iv);
//            this.iv = ivString;
//            Logger.i(appContext, TAG, "IV:" + ivString);
//            this.data = encryptedData;
//            this.encryptedKey = encryptedKeyString;
//        } catch (NoSuchAlgorithmException e) {
//            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidKeySpecException e) {
//            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (IllegalBlockSizeException e) {
//            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidKeyException e) {
//            Logger.e(appContext, TAG, "InvalidKeyException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (BadPaddingException e) {
//            Logger.e(appContext, TAG, "BadPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchPaddingException e) {
//            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (UnsupportedEncodingException e) {
//            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
//            throw new EncryptionException(e.getMessage());
//        }
//        try {
//            byte[] encryptedKeyBytes = Base64.decode(this.encryptedKey.getBytes(), Base64.DEFAULT);
//            PrivateKey privateKey = Crypto.generatePrivateKey(PRIVATE_KEY);
//            String secretKeyString = Crypto.decrypt(encryptedKeyBytes, privateKey);
//            Log.i(TAG,"AES====>"+secretKeyString);
//            SecretKey secretKey = Crypto.generateSecretKey(secretKeyString);
//            String decryptedData = Crypto.decryptWithSecretKey(this.data,secretKey);
//            Log.i(TAG,"decryptedData=====>"+decryptedData);
//        } catch (NoSuchPaddingException e) {
//            Log.e(TAG, "NoSuchPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (BadPaddingException e) {
//            Log.e(TAG, "BadPaddingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e(TAG, "NoSuchAlgorithmException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, "UnsupportedEncodingException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (IllegalBlockSizeException e) {
//            Log.e(TAG, "IllegalBlockSizeException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidKeyException e) {
//            Log.e(TAG, "InvalidKeyException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (InvalidKeySpecException e) {
//            Log.e(TAG, "InvalidKeySpecException", e);
//            throw new EncryptionException(e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception", e);
//            throw new EncryptionException(e.getMessage());
//        }
        Logger.i(appContext, TAG, "<-- ENCRYPTED");
    }

    public String getEncryptedKey() {
        return this.encryptedKey;
    }

    public String getIV() {
        return this.iv;
    }
}
