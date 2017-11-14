package org.ekstep.genieservices.tagservice;


import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sneha on 9/8/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TagServiceTest extends GenieServiceTestBase {
    private static final String TAG = TagServiceTest.class.getSimpleName();
    private static final String TAG_NAME = "test9tag";
    private static final String TAG_DESC = "this is test tag";
    private static final String START_DATE = "2017-08-8";
    private static final String END_DATE = "2017-08-12";

    @Override
    public void setup() throws IOException {
        super.setup();
        GenieServiceDBHelper.clearTagTableDBEntry();
    }

    @Override
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _11shouldCheckForSetTag() {
        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertTrue(genieResponse.getStatus());
    }

    @Test
    public void _12shouldValidateForEmptyTagName() {
        Tag tag = new Tag("", TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("Tag name can't be null or empty.", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void _22shouldCheckForGetTag() {

        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<List<Tag>> getTagResponse = activity.getTag();
        Assert.assertEquals(TAG_NAME, getTagResponse.getResult().get(0).getName());
        Assert.assertEquals(TAG_DESC, getTagResponse.getResult().get(0).getDescription());
        Assert.assertEquals(START_DATE, getTagResponse.getResult().get(0).getStartDate());
        Assert.assertEquals(END_DATE, getTagResponse.getResult().get(0).getEndDate());

    }

    @Test
    public void _33shouldCheckForUpdateTag() {
        String tag_desc = "new test description";

        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertTrue(genieResponse.getStatus());

        Tag updatedTag = new Tag(TAG_NAME, tag_desc, START_DATE, END_DATE);
        GenieResponse response = activity.updateTag(updatedTag);
        Assert.assertTrue(response.getStatus());
    }

    @Test
    public void _34shouldValidateForUpdateTag() {

        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);

        GenieResponse response = activity.updateTag(tag);
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals("Tag name not found.", response.getErrorMessages().get(0));
    }

    @Test
    public void _35shouldValidateForUpdateTagWhenForNullName() {
        String tag_desc = "new test description";

        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertTrue(genieResponse.getStatus());

        Tag updatedTag = new Tag("", tag_desc, START_DATE, END_DATE);
        GenieResponse response = activity.updateTag(updatedTag);
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals("Tag name can't be null or empty.", response.getErrorMessages().get(0));
    }

    @Test
    public void _44shouldCheckForDeleteTag() {
        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);
        GenieResponse genieResponse = activity.setTag(tag);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse response = activity.deleteTag(tag.getName());
        Assert.assertTrue(response.getStatus());

    }

    @Test
    public void _45shouldValidateForDeleteTag() {
        Tag tag = new Tag(TAG_NAME, TAG_DESC, START_DATE, END_DATE);

        GenieResponse response = activity.deleteTag(tag.getName());
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals("Tag name not found.", response.getErrorMessages().get(0));
    }
}


