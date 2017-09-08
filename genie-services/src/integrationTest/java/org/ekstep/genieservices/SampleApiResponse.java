package org.ekstep.genieservices;

/**
 * Created by swayangjit on 31/8/17.
 */

public class SampleApiResponse {


    public static String getSampleOEACESSEvent() {
        return "{\n" +
                "  \"ver\": \"2.1\",\n" +
                "  \"uid\": \"ecf0a070-df51-46c4-ba8c-2cbab1f500f6\",\n" +
                "  \"sid\": \"\",\n" +
                "  \"did\": \"\",\n" +
                "  \"edata\": {\n" +
                "    \"eks\": {\n" +
                "      \"qid\": \"ek.n.ib.m.t.2.6\",\n" +
                "      \"maxscore\": 1,\n" +
                "      \"params\": [\n" +
                "        \n" +
                "      ],\n" +
                "      \"score\": 0,\n" +
                "      \"pass\": \"No\",\n" +
                "      \"resvalues\": [\n" +
                "        {\n" +
                "          \"10\": \"true\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"uri\": \"\",\n" +
                "      \"qindex\": 9,\n" +
                "      \"exlength\": 0,\n" +
                "      \"qtitle\": \"Pick the correct answer\",\n" +
                "      \"qdesc\": \"\",\n" +
                "      \"mmc\": [\n" +
                "        \n" +
                "      ],\n" +
                "      \"mc\": [\n" +
                "        \n" +
                "      ],\n" +
                "      \"length\": 12\n" +
                "    }\n" +
                "  },\n" +
                "  \"eid\": \"OE_ASSESS\",\n" +
                "  \"gdata\": {\n" +
                "    \"id\": \"do_30013486\",\n" +
                "    \"ver\": \"4.0\"\n" +
                "  },\n" +
                "  \"cdata\": [\n" +
                "    {\n" +
                "      \"type\": \"collection\",\n" +
                "      \"id\": \"do_2121925679111454721253/do_30019820\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"02b860fc5ce415fcfb37d82472d1af43\",\n" +
                "      \"type\": \"ContentSession\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"channel\": \"in.ekstep\",\n" +
                "  \"etags\": {\n" +
                "    \"dims\": [\n" +
                "      \n" +
                "    ],\n" +
                "    \"app\": [\n" +
                "      \n" +
                "    ],\n" +
                "    \"partner\": [\n" +
                "      \n" +
                "    ]\n" +
                "  },\n" +
                "  \"pdata\": {\n" +
                "    \"id\": \"in.ekstep\",\n" +
                "    \"ver\": \"1.0\"\n" +
                "  },\n" +
                "  \"ets\": 1504775099183\n" +
                "}";
    }

    public static String getSampleOEEndEvent() {
        return "{\n" +
                "  \"ver\": \"2.1\",\n" +
                "  \"uid\": \"ecf0a070-df51-46c4-ba8c-2cbab1f500f6\",\n" +
                "  \"sid\": \"\",\n" +
                "  \"did\": \"\",\n" +
                "  \"edata\": {\n" +
                "    \"eks\": {\n" +
                "      \"length\": 137,\n" +
                "      \"progress\": 100,\n" +
                "      \"stageid\": \"ContentApp-Renderer\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"eid\": \"OE_END\",\n" +
                "  \"gdata\": {\n" +
                "    \"id\": \"do_30013486\",\n" +
                "    \"ver\": \"4.0\"\n" +
                "  },\n" +
                "  \"cdata\": [\n" +
                "    {\n" +
                "      \"type\": \"collection\",\n" +
                "      \"id\": \"do_2121925679111454721253/do_30019820\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"02b860fc5ce415fcfb37d82472d1af43\",\n" +
                "      \"type\": \"ContentSession\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"channel\": \"in.ekstep\",\n" +
                "  \"etags\": {\n" +
                "    \"dims\": [\n" +
                "      \n" +
                "    ],\n" +
                "    \"app\": [\n" +
                "      \n" +
                "    ],\n" +
                "    \"partner\": [\n" +
                "      \n" +
                "    ]\n" +
                "  },\n" +
                "  \"pdata\": {\n" +
                "    \"id\": \"in.ekstep\",\n" +
                "    \"ver\": \"1.0\"\n" +
                "  },\n" +
                "  \"ets\": 1504775216632\n" +
                "}";
    }

    public static String getContentDetailsResponse() {
        return "{\n" +
                "  \"id\": \"ekstep.content.find\",\n" +
                "  \"ver\": \"3.0\",\n" +
                "  \"ts\": \"2017-09-01T09:20:29ZZ\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"790d157f-b762-471d-b25c-eaf4497aa0af\",\n" +
                "    \"msgid\": null,\n" +
                "    \"err\": null,\n" +
                "    \"status\": \"successful\",\n" +
                "    \"errmsg\": null\n" +
                "  },\n" +
                "  \"responseCode\": \"OK\",\n" +
                "  \"result\": {\n" +
                "    \"content\": {\n" +
                "      \"copyright\": \"CC0\",\n" +
                "      \"keywords\": [\n" +
                "        \"APSSDC\",\n" +
                "        \"multiplication tables\",\n" +
                "        \"multiplication\",\n" +
                "        \"2 times table\",\n" +
                "        \"table of 2\",\n" +
                "        \"times table\",\n" +
                "        \"tables\"\n" +
                "      ],\n" +
                "      \"channel\": \"in.ekstep\",\n" +
                "      \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30013486_1467630893935.ecar\",\n" +
                "      \"language\": [\n" +
                "        \"English\"\n" +
                "      ],\n" +
                "      \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "      \"source\": \"EkStep\",\n" +
                "      \"gradeLevel\": [\n" +
                "        \"Grade 1\",\n" +
                "        \"Grade 2\",\n" +
                "        \"Grade 3\",\n" +
                "        \"Grade 4\",\n" +
                "        \"Grade 5\",\n" +
                "        \"Other\"\n" +
                "      ],\n" +
                "      \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/slide01_1466495038962.jpg\",\n" +
                "      \"me_totalTimespent\": 3022.09,\n" +
                "      \"me_averageTimespentPerSession\": 32.85,\n" +
                "      \"collections\": [\n" +
                "        \n" +
                "      ],\n" +
                "      \"me_totalRatings\": 42,\n" +
                "      \"contentEncoding\": \"gzip\",\n" +
                "      \"contentType\": \"Worksheet\",\n" +
                "      \"sYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:07:49.155+0000\",\n" +
                "      \"lastUpdatedBy\": \"Anuj\",\n" +
                "      \"identifier\": \"do_30013486\",\n" +
                "      \"audience\": [\n" +
                "        \"Instructor\"\n" +
                "      ],\n" +
                "      \"visibility\": \"Default\",\n" +
                "      \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "      \"portalOwner\": \"334\",\n" +
                "      \"mediaType\": \"content\",\n" +
                "      \"osId\": \"org.ekstep.quiz.app\",\n" +
                "      \"ageGroup\": [\n" +
                "        \"5-6\",\n" +
                "        \"6-7\",\n" +
                "        \"7-8\"\n" +
                "      ],\n" +
                "      \"languageCode\": \"en\",\n" +
                "      \"optStatus\": \"Complete\",\n" +
                "      \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "      \"lastPublishedOn\": \"2016-07-04T11:14:54.306+0000\",\n" +
                "      \"size\": 1505710.0,\n" +
                "      \"concepts\": [\n" +
                "        {\n" +
                "          \"identifier\": \"Num:C3:SC1:MC18\",\n" +
                "          \"name\": \"Mental maths; Single digit,\",\n" +
                "          \"objectType\": \"Concept\",\n" +
                "          \"relation\": \"associatedTo\",\n" +
                "          \"description\": \"Mental maths; Single digit,\",\n" +
                "          \"index\": null,\n" +
                "          \"status\": null,\n" +
                "          \"depth\": null,\n" +
                "          \"mimeType\": null,\n" +
                "          \"visibility\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"identifier\": \"Num:C3:SC3:MC8\",\n" +
                "          \"name\": \"multiplication tables\",\n" +
                "          \"objectType\": \"Concept\",\n" +
                "          \"relation\": \"associatedTo\",\n" +
                "          \"description\": \"multiplication tables\",\n" +
                "          \"index\": null,\n" +
                "          \"status\": null,\n" +
                "          \"depth\": null,\n" +
                "          \"mimeType\": null,\n" +
                "          \"visibility\": null\n" +
                "        }\n" +
                "      ],\n" +
                "      \"domain\": [\n" +
                "        \"numeracy\"\n" +
                "      ],\n" +
                "      \"me_averageSessionsPerDevice\": 4.6,\n" +
                "      \"name\": \"Multiplication - 2 Times Table\",\n" +
                "      \"publisher\": \"\",\n" +
                "      \"status\": \"Flagged\",\n" +
                "      \"template\": \"\",\n" +
                "      \"me_averageInteractionsPerMin\": 44.95,\n" +
                "      \"code\": \"org.ekstep.numeracy.worksheet.510\",\n" +
                "      \"me_totalSessionsCount\": 92,\n" +
                "      \"purpose\": \"instructor\",\n" +
                "      \"imageCredits\": [\n" +
                "        \"ekstep\",\n" +
                "        \"Parabal Singh\",\n" +
                "        \"EkStep\"\n" +
                "      ],\n" +
                "      \"description\": \"This worksheet contains 10 questions from the multiplication table of 2. It is for students learning multiplication tables of 2-digit numbers. For each question, feedback is provided.\",\n" +
                "      \"lastFlaggedOn\": \"2017-08-14T04:32:55.690+0000\",\n" +
                "      \"flaggedBy\": [\n" +
                "        \"Anuj\"\n" +
                "      ],\n" +
                "      \"idealScreenSize\": \"normal\",\n" +
                "      \"createdOn\": \"2016-06-20T16:54:48.856+0000\",\n" +
                "      \"me_totalSideloads\": 84,\n" +
                "      \"me_totalComments\": 2,\n" +
                "      \"popularity\": 3022.09,\n" +
                "      \"contentDisposition\": \"inline\",\n" +
                "      \"lastUpdatedOn\": \"2017-08-14T04:32:55.705+0000\",\n" +
                "      \"me_totalDevices\": 20,\n" +
                "      \"me_totalDownloads\": 192,\n" +
                "      \"owner\": \"Parabal Partap Singh\",\n" +
                "      \"creator\": \"Parabal Partap Singh\",\n" +
                "      \"flagReasons\": [\n" +
                "        \"Copyright Violation\"\n" +
                "      ],\n" +
                "      \"os\": [\n" +
                "        \"All\"\n" +
                "      ],\n" +
                "      \"soundCredits\": [\n" +
                "        \"ekstep\"\n" +
                "      ],\n" +
                "      \"me_totalInteractions\": 2264,\n" +
                "      \"pkgVersion\": 4.0,\n" +
                "      \"versionKey\": \"1502685175705\",\n" +
                "      \"idealScreenDensity\": \"hdpi\",\n" +
                "      \"s3Key\": \"ecar_files/do_30013486_1467630893935.ecar\",\n" +
                "      \"me_averageRating\": 4.26,\n" +
                "      \"lastSubmittedOn\": \"2016-07-04T10:00:24.306+0000\",\n" +
                "      \"createdBy\": \"334\",\n" +
                "      \"compatibilityLevel\": 1,\n" +
                "      \"usedByContent\": [\n" +
                "        \n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    public static String getRecommendedContent() {
        return "{\n" +
                "  \"id\": \"ekstep.analytics.recommendations\",\n" +
                "  \"ver\": \"1.0\",\n" +
                "  \"ts\": \"2017-09-06T07:55:48.412+00:00\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"a8b351cc-c5f2-4b9b-b253-a496fbc7f215\",\n" +
                "    \"status\": \"successful\"\n" +
                "  },\n" +
                "  \"responseCode\": \"OK\",\n" +
                "  \"result\": {\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"New_bundle\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2017-05-09T05:31:41.514+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"lastUpdatedOn\": \"2017-06-05T04:44:20.594+0000\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 717758.0,\n" +
                "        \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "        \"identifier\": \"LP_FT_006\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Books for learning about colours, animals, fruits, vegetables, shapes\",\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"tags\": [\n" +
                "          \"colours\",\n" +
                "          \"animals\",\n" +
                "          \" fruits\",\n" +
                "          \"vegetables\",\n" +
                "          \"shapes\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 314.47,\n" +
                "        \"node_id\": 0.0,\n" +
                "        \"me_totalInteractions\": 158.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \"LP_FT_001Collection\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 4.0,\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-09T00:41:49.648+0000\",\n" +
                "        \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "        \"me_averageInteractionsPerMin\": 30.15,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/LP_FT_006/new_bundle_1494308052024_lp_ft_006_1.0.ecar\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"versionKey\": \"1496968909648\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"code\": \"org.ekstep.feb03.story.learningbooks\",\n" +
                "        \"me_totalDownloads\": 6.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"prevState\": \"Draft\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 78.62,\n" +
                "        \"lastPublishedOn\": \"2017-05-09T05:34:12.024+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"LP_FT_006\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"keywords\": [\n" +
                "          \"shapes\",\n" +
                "          \"vegetables\",\n" +
                "          \" fruits\",\n" +
                "          \"animals\",\n" +
                "          \"colours\"\n" +
                "        ],\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"me_imagesCount\": 1.0,\n" +
                "        \"me_audiosCount\": 0.0,\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"IL_UNIQUE_ID\": \"LP_FT_006\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/LP_FT_006/new_bundle_1494308052024_lp_ft_006_1.0.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 3.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/lp_ft_006/artifact/uploadcontent_1494307940414.zip\",\n" +
                "        \"me_averageSessionsPerDevice\": 1.33,\n" +
                "        \"reco_score\": 2.87,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/LP_FT_006/new_bundle_1494308052192_lp_ft_006_1.0_spine.ecar\\\",\\\"size\\\":793.0}}\",\n" +
                "        \"pkgVersion\": 1.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"popularity\": 12.61,\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"b\",\n" +
                "        \"createdOn\": \"2017-01-07T08:52:39.070+0000\",\n" +
                "        \"channel\": \"505c7c48ac6dc1edc9b08f21db5a571d\",\n" +
                "        \"source\": \"\",\n" +
                "        \"lastUpdatedOn\": \"2017-08-31T12:21:03.596+0000\",\n" +
                "        \"size\": 37892.0,\n" +
                "        \"identifier\": \"do_212155118871101440166\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"b\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Grade 1\"\n" +
                "        ],\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 12.61,\n" +
                "        \"node_id\": 47549.0,\n" +
                "        \"me_totalInteractions\": 11.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \"do_20052190\",\n" +
                "          \"do_2122676615775764481183\",\n" +
                "          \"do_2123228935551959041197\",\n" +
                "          \"do_21232299526136627211056\",\n" +
                "          \"do_2123242743311155201348\",\n" +
                "          \"do_2123249175889674241410\",\n" +
                "          \"do_2123250158331985921500\",\n" +
                "          \"do_2123250226715688961520\",\n" +
                "          \"do_2123250892046663681618\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 2.0,\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_212155118871101440166/artifact/apple_239_1483680875_1483680875225.thumb.jpg\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:18:21.039+0000\",\n" +
                "        \"me_averageInteractionsPerMin\": 52.34,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_212155118871101440166/b_1483782013636_do_212155118871101440166_1.0.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \"LO53\"\n" +
                "        ],\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"posterImage\": \"https://qa.ekstep.in/assets/public/content/do_212154313729384448213/artifact/apple_239_1483680875_1483680875225.jpg\",\n" +
                "        \"creator\": \"Vignesh Pandi\",\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"versionKey\": \"1504182063596\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"361\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.1838\",\n" +
                "        \"me_totalDownloads\": 7.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 6.31,\n" +
                "        \"lastPublishedOn\": \"2017-01-07T09:40:13.835+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"do_212155118871101440166\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"Public User\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"361\",\n" +
                "        \"lastSubmittedOn\": \"2017-01-07T09:30:03.150+0000\",\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"lastPublishedBy\": \"399\",\n" +
                "        \"owner\": \"sqs ek\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"sunbird.portal\",\n" +
                "        \"s3Key\": \"ecar_files/do_212155118871101440166/b_1483782013636_do_212155118871101440166_1.0.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 2.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_212155118871101440166/artifact/1483779374020_do_212155118871101440166.zip\",\n" +
                "        \"lastFlaggedOn\": \"2017-08-30T11:15:37.227+0000\",\n" +
                "        \"flaggedBy\": [\n" +
                "          \"Public User\"\n" +
                "        ],\n" +
                "        \"me_averageSessionsPerDevice\": 1.0,\n" +
                "        \"reco_score\": 2.85,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"pkgVersion\": 1.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"ESL Lesson 3 part 1\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2017-05-25T09:52:56.297+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"\",\n" +
                "        \"lastUpdatedOn\": \"2017-06-05T04:34:03.944+0000\",\n" +
                "        \"subject\": \"\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 8113815.0,\n" +
                "        \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "        \"identifier\": \"do_2122528233578250241233\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"els l3p1\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\"\n" +
                "        ],\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 1126.42,\n" +
                "        \"node_id\": 0.0,\n" +
                "        \"me_totalInteractions\": 404.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \"do_212262094228291584130\",\n" +
                "          \"do_212265550213799936168\",\n" +
                "          \"do_21226836960507494418\",\n" +
                "          \"do_212282811101773824183\",\n" +
                "          \"do_212282814589853696190\",\n" +
                "          \"do_212282810555342848180\",\n" +
                "          \"do_212282810437918720179\",\n" +
                "          \"do_2122881050396508161323\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 18.0,\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122528233578250241233/artifact/e34ddd91a14bf497b153e64d018f6998_1495705925435.thumb.jpeg\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T07:16:57.991+0000\",\n" +
                "        \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "        \"me_averageInteractionsPerMin\": 21.52,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122528233578250241233/esl-lesson-3-part-1_1495706475819_do_2122528233578250241233_1.0.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"medium\": \"\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122528229410406401232/artifact/e34ddd91a14bf497b153e64d018f6998_1495705925435.jpeg\",\n" +
                "        \"creator\": \"Srivathsa Dhanraj\",\n" +
                "        \"me_totalSideloads\": 2.0,\n" +
                "        \"versionKey\": \"1497251817991\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"200\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.6369\",\n" +
                "        \"me_totalDownloads\": 22.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"prevState\": \"Review\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"<5\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 62.58,\n" +
                "        \"lastPublishedOn\": \"2017-05-25T10:01:15.818+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"do_2122528233578250241233\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"329\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"200\",\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"lastPublishedBy\": \"329\",\n" +
                "        \"me_imagesCount\": 75.0,\n" +
                "        \"me_audiosCount\": 66.0,\n" +
                "        \"owner\": \"Srivathsa\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"IL_UNIQUE_ID\": \"do_2122528233578250241233\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/do_2122528233578250241233/esl-lesson-3-part-1_1495706475819_do_2122528233578250241233_1.0.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 8.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122528233578250241233/artifact/3p1_200_1495706019_1495705976650.zip\",\n" +
                "        \"me_averageSessionsPerDevice\": 2.25,\n" +
                "        \"reco_score\": 2.81,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122528233578250241233/esl-lesson-3-part-1_1495706477065_do_2122528233578250241233_1.0_spine.ecar\\\",\\\"size\\\":2097.0}}\",\n" +
                "        \"pkgVersion\": 1.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"Keep live - test 12\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2017-05-11T05:15:15.676+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"lastUpdatedOn\": \"2017-06-05T04:37:30.665+0000\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 7351264.0,\n" +
                "        \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "        \"identifier\": \"do_2122427776796999681130\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Edit 1\",\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 117.72,\n" +
                "        \"node_id\": 0.0,\n" +
                "        \"me_totalInteractions\": 22.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 2.0,\n" +
                "        \"languageCode\": \"en\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T07:17:02.367+0000\",\n" +
                "        \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "        \"me_averageInteractionsPerMin\": 11.21,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122427776796999681130/keep-live-test-12_1494479740334_do_2122427776796999681130_2.0.ecar\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"creator\": \"Manoj Londhe\",\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"versionKey\": \"1497251822367\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"177\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.4360\",\n" +
                "        \"me_totalDownloads\": 2.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"prevState\": \"Draft\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 58.86,\n" +
                "        \"lastPublishedOn\": \"2017-05-11T05:15:40.333+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"do_2122427776796999681130\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"Ekstep\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"177\",\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"lastPublishedBy\": \"Ekstep\",\n" +
                "        \"me_imagesCount\": 25.0,\n" +
                "        \"me_audiosCount\": 0.0,\n" +
                "        \"owner\": \"Author\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"IL_UNIQUE_ID\": \"do_2122427776796999681130\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/do_2122427776796999681130/keep-live-test-12_1494479740334_do_2122427776796999681130_2.0.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 2.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122427776796999681130/artifact/custom_plugin_1494479715648.zip\",\n" +
                "        \"me_averageSessionsPerDevice\": 1.0,\n" +
                "        \"reco_score\": 2.76,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122427776796999681130/keep-live-test-12_1494479741561_do_2122427776796999681130_2.0_spine.ecar\\\",\\\"size\\\":789.0}}\",\n" +
                "        \"pkgVersion\": 2.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"popularity\": 2510.23,\n" +
                "        \"author\": \"Pankaj Chaturvedi\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"Look At Me\",\n" +
                "        \"optStatus\": \"Complete\",\n" +
                "        \"createdOn\": \"2016-06-14T04:46:42.505+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"MangoReader\",\n" +
                "        \"lastUpdatedOn\": \"2017-06-05T04:42:03.260+0000\",\n" +
                "        \"size\": 5302722.0,\n" +
                "        \"identifier\": \"domain_8808\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Meet the children in the book.They are just like you! Children can read and listen to the story. This story is appropriate for children just beginning to read sentences. \",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\"\n" +
                "        ],\n" +
                "        \"tags\": [\n" +
                "          \"prathamdigitalcamal\",\n" +
                "          \"Friendship\",\n" +
                "          \"Morals and values\",\n" +
                "          \"Family and friends\",\n" +
                "          \"Bedtime stories\",\n" +
                "          \"MangoSense\",\n" +
                "          \"Pratham Books\",\n" +
                "          \"School time\",\n" +
                "          \"Action and adventure\",\n" +
                "          \"APSSDC\"\n" +
                "        ],\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 12200.98,\n" +
                "        \"node_id\": 8658.0,\n" +
                "        \"me_totalInteractions\": 2166.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": \"Learner\",\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"developer\": \"EkStep\",\n" +
                "        \"me_totalSessionsCount\": 200.0,\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/1461668536884adb212cfde_1465896981928.jpg\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-04T15:07:33.002+0000\",\n" +
                "        \"me_averageInteractionsPerMin\": 10.65,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/domain_8808_1466135734049.ecar\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"creator\": \"Pratham\",\n" +
                "        \"me_totalSideloads\": 23.0,\n" +
                "        \"versionKey\": \"1496637723260\",\n" +
                "        \"purpose\": \"instructor\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"366\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.412\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"me_totalDownloads\": 76.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\",\n" +
                "          \"6-7\",\n" +
                "          \"7-8\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 61.0,\n" +
                "        \"lastPublishedOn\": \"2016-06-17T03:55:34.688+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"domain_8808\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"286\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"Pratham Books\",\n" +
                "        \"imageCredits\": [\n" +
                "          \"ekstep\",\n" +
                "          \"Harish S C\"\n" +
                "        ],\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"366\",\n" +
                "        \"lastSubmittedOn\": \"2016-06-17T03:55:03.461+0000\",\n" +
                "        \"keywords\": [\n" +
                "          \"APSSDC\",\n" +
                "          \"prathamdigitalcamal\",\n" +
                "          \"Friendship\",\n" +
                "          \"Bedtime stories\",\n" +
                "          \"Morals and values\",\n" +
                "          \"Family and friends\",\n" +
                "          \"School time\",\n" +
                "          \"Action and adventure\",\n" +
                "          \"MangoSense\",\n" +
                "          \"Pratham Books\"\n" +
                "        ],\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"copyType\": \"Enhance\",\n" +
                "        \"owner\": \"Pankaj Chaturvedi\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/domain_8808_1466135734049.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"soundCredits\": [\n" +
                "          \"ekstep\"\n" +
                "        ],\n" +
                "        \"me_totalDevices\": 25.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1466135731921_domain_8808.zip\",\n" +
                "        \"me_averageSessionsPerDevice\": 8.0,\n" +
                "        \"reco_score\": 2.67,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"pkgVersion\": 11.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"test_srivakeyboard\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2016-08-16T08:54:39.969+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"\",\n" +
                "        \"lastUpdatedOn\": \"2017-05-15T07:06:42.913+0000\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 605239.0,\n" +
                "        \"identifier\": \"do_20045417\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"test_srivakeyboard\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\",\n" +
                "          \"Grade 2\",\n" +
                "          \"Grade 3\",\n" +
                "          \"Grade 4\",\n" +
                "          \"Grade 5\",\n" +
                "          \"Other\"\n" +
                "        ],\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"node_id\": 43443.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1455104397576flowersl.thumb.png\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:22:41.913+0000\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_srivakeyboard_1471337910402_do_20045417.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \"LO52\"\n" +
                "        ],\n" +
                "        \"posterImage\": \"https://qa.ekstep.in/assets/public/content/1455104397576flowersL.png\",\n" +
                "        \"creator\": \"Debasis Singh\",\n" +
                "        \"versionKey\": \"1497244961913\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"347\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.1785\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\"\n" +
                "        ],\n" +
                "        \"lastPublishedOn\": \"2016-08-16T08:58:30.742+0000\",\n" +
                "        \"es_metadata_id\": \"do_20045417\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"400\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"347\",\n" +
                "        \"lastSubmittedOn\": \"2016-08-16T08:56:37.114+0000\",\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"do_20043409\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"me_imagesCount\": 1.0,\n" +
                "        \"me_audiosCount\": 0.0,\n" +
                "        \"owner\": \"Debasis Singh\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/test_srivakeyboard_1471337910402_do_20045417.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/test_sriva_347_1471337683_1471337776394.zip\",\n" +
                "        \"reco_score\": 2.49,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"pkgVersion\": 1.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"popularity\": 1420.36,\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"test_prathamstoriesthirdday\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2016-07-15T07:06:47.390+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"\",\n" +
                "        \"lastUpdatedOn\": \"2017-05-15T07:05:27.555+0000\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 1.3397423E7,\n" +
                "        \"identifier\": \"do_20043361\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"test_prathamstoriesthirdday\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\",\n" +
                "          \"Grade 2\",\n" +
                "          \"Grade 3\",\n" +
                "          \"Grade 4\",\n" +
                "          \"Grade 5\",\n" +
                "          \"Other\"\n" +
                "        ],\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 1420.36,\n" +
                "        \"node_id\": 41480.0,\n" +
                "        \"me_totalInteractions\": 527.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 17.0,\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/mic_347_1468566427_1468566462995.thumb.png\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:16:45.444+0000\",\n" +
                "        \"me_averageInteractionsPerMin\": 22.26,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_prathamstoriesthirdday_1470908490842_do_20043361.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \"LO7\"\n" +
                "        ],\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/mic_347_1468566427_1468566462995.png\",\n" +
                "        \"creator\": \"Debasis Singh\",\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"versionKey\": \"1497244605444\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"347\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.1215\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"me_totalDownloads\": 13.0,\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 83.55,\n" +
                "        \"lastPublishedOn\": \"2016-08-11T09:41:32.769+0000\",\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"es_metadata_id\": \"do_20043361\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"400\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"347\",\n" +
                "        \"lastSubmittedOn\": \"2016-08-11T09:40:49.500+0000\",\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"domain_12843\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"me_imagesCount\": 36.0,\n" +
                "        \"me_audiosCount\": 0.0,\n" +
                "        \"owner\": \"Debasis Singh\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/test_prathamstoriesthirdday_1470908490842_do_20043361.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 8.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/pratham3_347_1470908344_1470908428406.zip\",\n" +
                "        \"me_averageSessionsPerDevice\": 2.13,\n" +
                "        \"reco_score\": 2.42,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"pkgVersion\": 4.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"Test892016-06\",\n" +
                "        \"createdOn\": \"2016-09-08T07:10:57.578+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"\",\n" +
                "        \"genre\": [\n" +
                "          \"Chapter Books\",\n" +
                "          \"Alphabet Books\"\n" +
                "        ],\n" +
                "        \"lastUpdatedOn\": \"2017-05-15T07:07:22.888+0000\",\n" +
                "        \"size\": 489916.0,\n" +
                "        \"identifier\": \"do_20046420\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Test\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\",\n" +
                "          \"Grade 2\",\n" +
                "          \"Grade 3\",\n" +
                "          \"Grade 4\",\n" +
                "          \"Grade 5\",\n" +
                "          \"Other\"\n" +
                "        ],\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"node_id\": 44249.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1455104093251circle3.thumb.png\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:26:37.129+0000\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test892016-06_1473326398209_do_20046420.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \"LO51\",\n" +
                "          \"LO29\",\n" +
                "          \"LO53\"\n" +
                "        ],\n" +
                "        \"posterImage\": \"https://qa.ekstep.in/assets/public/content/1455104093251circle3.png\",\n" +
                "        \"creator\": \"External Testing vendor\",\n" +
                "        \"versionKey\": \"1497245197129\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"239\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.2085\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\"\n" +
                "        ],\n" +
                "        \"lastPublishedOn\": \"2016-09-08T09:19:58.449+0000\",\n" +
                "        \"es_metadata_id\": \"do_20046420\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"434\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"imageCredits\": [\n" +
                "          \"ekstep\"\n" +
                "        ],\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"239\",\n" +
                "        \"lastSubmittedOn\": \"2016-09-08T09:16:52.730+0000\",\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"do_20043409\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"owner\": \"External Testing vendor\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/test892016-06_1473326398209_do_20046420.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"theme\": [\n" +
                "          \"Adventure\",\n" +
                "          \"Geography\"\n" +
                "        ],\n" +
                "        \"soundCredits\": [\n" +
                "          \"ekstep\"\n" +
                "        ],\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1473326397800_do_20046420.zip\",\n" +
                "        \"reco_score\": 2.41,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"pkgVersion\": 1.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"Learning Books\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"createdOn\": \"2017-04-27T06:00:54.810+0000\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"lastUpdatedOn\": \"2017-04-27T06:15:51.822+0000\",\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"size\": 717844.0,\n" +
                "        \"identifier\": \"LP_FT_001\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Books for learning about colours, animals, fruits, vegetables, shapes\",\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"tags\": [\n" +
                "          \"colours\",\n" +
                "          \"animals\",\n" +
                "          \" fruits\",\n" +
                "          \"vegetables\",\n" +
                "          \"shapes\"\n" +
                "        ],\n" +
                "        \"node_id\": 53275.0,\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-09T00:40:47.403+0000\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/LP_FT_001/learning-books_1493273452738_lp_ft_001_3.0.ecar\",\n" +
                "        \"versionKey\": \"1496968847403\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"code\": \"org.ekstep.feb03.story.learningbooks\",\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"prevState\": \"Review\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"lastPublishedOn\": \"2017-04-27T06:10:52.738+0000\",\n" +
                "        \"es_metadata_id\": \"LP_FT_001\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"lastSubmittedOn\": \"2017-04-27T06:10:26.071+0000\",\n" +
                "        \"keywords\": [\n" +
                "          \"shapes\",\n" +
                "          \"vegetables\",\n" +
                "          \" fruits\",\n" +
                "          \"animals\",\n" +
                "          \"colours\"\n" +
                "        ],\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"me_imagesCount\": 26.0,\n" +
                "        \"me_audiosCount\": 0.0,\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"content/lp_ft_001/artifact/data_json_ecml_1493273733007.zip\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/lp_ft_001/artifact/data_json_ecml_1493273733007.zip\",\n" +
                "        \"reco_score\": 2.41,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/LP_FT_001/learning-books_1493273453006_lp_ft_001_3.0_spine.ecar\\\",\\\"size\\\":879.0}}\",\n" +
                "        \"pkgVersion\": 3.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"organization\": [\n" +
                "          \"\"\n" +
                "        ],\n" +
                "        \"popularity\": 118.78,\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"name\": \"Unsupported plugin issue\",\n" +
                "        \"createdOn\": \"2017-07-06T18:52:22.661+0000\",\n" +
                "        \"createdFor\": [\n" +
                "          \"\"\n" +
                "        ],\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"source\": \"\",\n" +
                "        \"lastUpdatedOn\": \"2017-07-05T12:17:40.781+0000\",\n" +
                "        \"subject\": \"\",\n" +
                "        \"size\": 532943.0,\n" +
                "        \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "        \"editorState\": \"{\\\"plugin\\\":{\\\"noOfExtPlugins\\\":2,\\\"extPlugins\\\":[{\\\"plugin\\\":\\\"org.ekstep.ceheader\\\",\\\"version\\\":\\\"1.0\\\"},{\\\"plugin\\\":\\\"org.ekstep.keyboardshortcuts\\\",\\\"version\\\":\\\"1.0\\\"}]},\\\"stage\\\":{\\\"noOfStages\\\":4,\\\"currentStage\\\":\\\"c5c263c4-2829-4d26-912a-5d627c0a5bc4\\\"},\\\"sidebar\\\":{\\\"selectedMenu\\\":\\\"settings\\\"}}\",\n" +
                "        \"identifier\": \"do_2122564688891904001125\",\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"description\": \"Unsupported plugin issue\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Grade 1\"\n" +
                "        ],\n" +
                "        \"domain\": [\n" +
                "          \"numeracy\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 118.78,\n" +
                "        \"node_id\": 55088.0,\n" +
                "        \"me_totalInteractions\": 8.0,\n" +
                "        \"compatibilityLevel\": 2.0,\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"collections\": [\n" +
                "          \"do_212283259737751552199\",\n" +
                "          \"do_2122832606725242881100\"\n" +
                "        ],\n" +
                "        \"me_totalSessionsCount\": 1.0,\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122564688891904001125/artifact/6bdd23ce6dc3a26e72b2cc64bf369d2f_1496312563658.thumb.jpeg\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-07-06T18:52:23.517+0000\",\n" +
                "        \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "        \"me_averageInteractionsPerMin\": 4.04,\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122564688891904001125/unsupported-plugin-issue_1499367142808_do_2122564688891904001125_2.0.ecar\",\n" +
                "        \"concepts\": [\n" +
                "          \"Num:C3:SC1\"\n" +
                "        ],\n" +
                "        \"medium\": \"English\",\n" +
                "        \"attributions\": [\n" +
                "          \"\"\n" +
                "        ],\n" +
                "        \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122577925213798401247/artifact/6bdd23ce6dc3a26e72b2cc64bf369d2f_1496312563658.jpeg\",\n" +
                "        \"creator\": \"Sunil A S\",\n" +
                "        \"versionKey\": \"1499367143517\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"portalOwner\": \"440\",\n" +
                "        \"code\": \"org.ekstep.literacy.story.6540\",\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"prevState\": \"Draft\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"ageGroup\": [\n" +
                "          \"<5\"\n" +
                "        ],\n" +
                "        \"me_averageTimespentPerSession\": 118.78,\n" +
                "        \"lastPublishedOn\": \"2017-07-06T18:52:22.808+0000\",\n" +
                "        \"es_metadata_id\": \"do_2122564688891904001125\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"lastUpdatedBy\": \"316\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"createdBy\": \"440\",\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"template\": \"\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"lastPublishedBy\": \"316\",\n" +
                "        \"owner\": \"Sunil\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"IL_UNIQUE_ID\": \"do_2122564688891904001125\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"s3Key\": \"ecar_files/do_2122564688891904001125/unsupported-plugin-issue_1499367142808_do_2122564688891904001125_2.0.ecar\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"me_totalDevices\": 1.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122564688891904001125/artifact/1499366399285_do_2122564688891904001125.zip\",\n" +
                "        \"collaborators\": [\n" +
                "          \"439\"\n" +
                "        ],\n" +
                "        \"me_averageSessionsPerDevice\": 1.0,\n" +
                "        \"reco_score\": 2.3,\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122564688891904001125/unsupported-plugin-issue_1499367143023_do_2122564688891904001125_2.0_spine.ecar\\\",\\\"size\\\":125738.0}}\",\n" +
                "        \"pkgVersion\": 2.0,\n" +
                "        \"idealScreenDensity\": \"hdpi\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"count\": 470\n" +
                "  }\n" +
                "}";
    }

    public static String getSerachAPIResult() {
        return "{\n" +
                "  \"id\": \"ekstep.composite-search.search\",\n" +
                "  \"ver\": \"3.0\",\n" +
                "  \"ts\": \"2017-09-07T17:20:17ZZ\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"5fdff53c-76d0-4c1d-9775-31f509ac8d10\",\n" +
                "    \"msgid\": null,\n" +
                "    \"err\": null,\n" +
                "    \"status\": \"successful\",\n" +
                "    \"errmsg\": null\n" +
                "  },\n" +
                "  \"responseCode\": \"OK\",\n" +
                "  \"result\": {\n" +
                "    \"count\": 588,\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/domain_4083_1467611093594.ecar\",\n" +
                "        \"language\": [\n" +
                "          \"Hindi\"\n" +
                "        ],\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"source\": \"Pratham.\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/Story5_1463740696341.png\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\",\n" +
                "          \"Grade 2\",\n" +
                "          \"Grade 3\",\n" +
                "          \"Grade 4\",\n" +
                "          \"Grade 5\",\n" +
                "          \"Other\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 31697.8,\n" +
                "        \"me_averageTimespentPerSession\": 54.94,\n" +
                "        \"collections\": [\n" +
                "          \"do_2123178654177198081571\"\n" +
                "        ],\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1467611087192_domain_4083.zip\",\n" +
                "        \"collaborators\": [\n" +
                "          \"206\",\n" +
                "          \"141\",\n" +
                "          \"224\",\n" +
                "          \"286\",\n" +
                "          \"350\"\n" +
                "        ],\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"lastUpdatedBy\": \"141\",\n" +
                "        \"identifier\": \"domain_4083\",\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"portalOwner\": \"EkStep\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\",\n" +
                "          \"6-7\",\n" +
                "          \"7-8\",\n" +
                "          \"8-10\"\n" +
                "        ],\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"tags\": [\n" +
                "          \"Camp - 1 (Basic)\",\n" +
                "          \"Story - 5\",\n" +
                "          \"camal story #5\",\n" +
                "          \"record and play\",\n" +
                "          \"record\",\n" +
                "          \"para level\",\n" +
                "          \"story level\",\n" +
                "          \"story\",\n" +
                "          \"para and story level\",\n" +
                "          \"kutha and roti\",\n" +
                "          \"kutha aur roti\",\n" +
                "          \"para\",\n" +
                "          \"camal story\",\n" +
                "          \"kutta\",\n" +
                "          \"kutta aur roti\",\n" +
                "          \"kutha\",\n" +
                "          \"roti\",\n" +
                "          \"hindi\",\n" +
                "          \"camal\",\n" +
                "          \"hindi stories\",\n" +
                "          \"hindi story\",\n" +
                "          \"pratham camal\",\n" +
                "          \"pratham\",\n" +
                "          \"40-60 words\"\n" +
                "        ],\n" +
                "        \"optStatus\": \"Complete\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"lastPublishedOn\": \"2016-07-04T05:44:54.368+0000\",\n" +
                "        \"size\": 6011039.0,\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"name\": \"कुत्ता और रोटी\",\n" +
                "        \"me_averageSessionsPerDevice\": 10.12,\n" +
                "        \"publisher\": \"\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"me_averageInteractionsPerMin\": 27.31,\n" +
                "        \"code\": \"org.ekstep.literacy.story.267\",\n" +
                "        \"me_totalSessionsCount\": 577.0,\n" +
                "        \"imageCredits\": [\n" +
                "          \"ekstep\",\n" +
                "          \"323\",\n" +
                "          \"Ekstep\",\n" +
                "          \"External\"\n" +
                "        ],\n" +
                "        \"description\": \"एक कुत्ते को रोटी मिली। पढ़िए और देखिये की उसने आगे क्या किया। कहानी उन बच्चों के लिए है जो अभी वाक्य  पढ़ना शुरू कर रहे हैं। कहानी के कठिन शब्द का अभ्यास कराया जा सकता हैं। कहानी अपनी आवाज़ में रिकॉर्ड करें और सुनें। अंत में कुछ सवाल हैं जो कहानी की समझ परखते हैं।\",\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"createdOn\": \"2016-05-02T10:03:16.029+0000\",\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"popularity\": 23390.78,\n" +
                "        \"genre\": [\n" +
                "          \"Fiction\"\n" +
                "        ],\n" +
                "        \"lastUpdatedOn\": \"2017-05-15T07:03:32.645+0000\",\n" +
                "        \"me_totalDevices\": 57.0,\n" +
                "        \"me_totalDownloads\": 3.0,\n" +
                "        \"owner\": \"Pratham\",\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"soundCredits\": [\n" +
                "          \"323\",\n" +
                "          \"141\",\n" +
                "          \"Aditya R\",\n" +
                "          \"ekstep\",\n" +
                "          \"Ekstep\",\n" +
                "          \"EkStep\"\n" +
                "        ],\n" +
                "        \"me_totalInteractions\": 14426.0,\n" +
                "        \"pkgVersion\": 15.0,\n" +
                "        \"versionKey\": \"1496647529372\",\n" +
                "        \"idealScreenDensity\": \"hdpi\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"lastSubmittedOn\": \"2016-07-04T04:31:16.477+0000\",\n" +
                "        \"developer\": \"EkStep\",\n" +
                "        \"node_id\": 0.0,\n" +
                "        \"tempData\": \"14\",\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"creator\": \"Ekstep\",\n" +
                "        \"createdBy\": \"EkStep\",\n" +
                "        \"audience\": \"Learner\",\n" +
                "        \"copyType\": \"Enhance\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-05T07:25:29.372+0000\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"author\": \"Pratham\",\n" +
                "        \"template\": \"domain_3946\",\n" +
                "        \"lastPublishDate\": \"2016-05-20T11:31:52.485+0000\",\n" +
                "        \"s3Key\": \"ecar_files/domain_4083_1467611093594.ecar\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"keywords\": [\n" +
                "          \"Story - 5\",\n" +
                "          \"Camp - 1 (Basic)\",\n" +
                "          \"camal story #5\",\n" +
                "          \"record\",\n" +
                "          \"record and play\",\n" +
                "          \"pratham\",\n" +
                "          \"pratham camal\",\n" +
                "          \"camal\",\n" +
                "          \"hindi\",\n" +
                "          \"hindi stories\",\n" +
                "          \"hindi story\",\n" +
                "          \"kutta\",\n" +
                "          \"kutta aur roti\",\n" +
                "          \"kutha\",\n" +
                "          \"roti\",\n" +
                "          \"kutha and roti\",\n" +
                "          \"kutha aur roti\",\n" +
                "          \"para\",\n" +
                "          \"camal story\",\n" +
                "          \"para and story level\",\n" +
                "          \"story\",\n" +
                "          \"story level\",\n" +
                "          \"para level\",\n" +
                "          \"40-60 words\"\n" +
                "        ],\n" +
                "        \"es_metadata_id\": \"domain_4083\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"copyright\": \"\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_zoomannerssecondpart_1475067339226_do_20043382.ecar\",\n" +
                "        \"language\": [\n" +
                "          \"English\"\n" +
                "        ],\n" +
                "        \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "        \"source\": \"\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/adjective_347_1468580176_1468580212549.thumb.png\",\n" +
                "        \"gradeLevel\": [\n" +
                "          \"Kindergarten\",\n" +
                "          \"Grade 1\",\n" +
                "          \"Grade 2\",\n" +
                "          \"Grade 3\",\n" +
                "          \"Grade 4\",\n" +
                "          \"Grade 5\",\n" +
                "          \"Other\"\n" +
                "        ],\n" +
                "        \"me_totalTimespent\": 15789.22,\n" +
                "        \"me_averageTimespentPerSession\": 112.78,\n" +
                "        \"me_totalRatings\": 0.0,\n" +
                "        \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/zoomanners-part2_new_347_1475067065_1475067237026.zip\",\n" +
                "        \"contentType\": \"Story\",\n" +
                "        \"lastUpdatedBy\": \"381\",\n" +
                "        \"identifier\": \"do_20043382\",\n" +
                "        \"visibility\": \"Default\",\n" +
                "        \"portalOwner\": \"347\",\n" +
                "        \"mediaType\": \"content\",\n" +
                "        \"osId\": \"org.ekstep.quiz.app\",\n" +
                "        \"ageGroup\": [\n" +
                "          \"5-6\"\n" +
                "        ],\n" +
                "        \"graph_id\": \"domain\",\n" +
                "        \"nodeType\": \"DATA_NODE\",\n" +
                "        \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "        \"lastPublishedOn\": \"2016-09-28T12:55:40.970+0000\",\n" +
                "        \"size\": 1.1923521E7,\n" +
                "        \"concepts\": [\n" +
                "          \"LO52\"\n" +
                "        ],\n" +
                "        \"domain\": [\n" +
                "          \"literacy\"\n" +
                "        ],\n" +
                "        \"me_averageSessionsPerDevice\": 11.67,\n" +
                "        \"name\": \"test_zoomannerssecondpart\",\n" +
                "        \"publisher\": \"\",\n" +
                "        \"status\": \"Live\",\n" +
                "        \"me_averageInteractionsPerMin\": 32.55,\n" +
                "        \"code\": \"org.ekstep.literacy.story.1226\",\n" +
                "        \"me_totalSessionsCount\": 140.0,\n" +
                "        \"description\": \"test_zoomannerssecondpart\",\n" +
                "        \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/adjective_347_1468580176_1468580212549.png\",\n" +
                "        \"idealScreenSize\": \"normal\",\n" +
                "        \"createdOn\": \"2016-07-15T10:56:03.393+0000\",\n" +
                "        \"me_totalSideloads\": 0.0,\n" +
                "        \"me_totalComments\": 0.0,\n" +
                "        \"popularity\": 15789.22,\n" +
                "        \"lastUpdatedOn\": \"2017-05-15T07:05:32.704+0000\",\n" +
                "        \"me_totalDevices\": 12.0,\n" +
                "        \"owner\": \"Debasis Singh\",\n" +
                "        \"me_totalDownloads\": 10.0,\n" +
                "        \"os\": [\n" +
                "          \"All\"\n" +
                "        ],\n" +
                "        \"me_totalInteractions\": 8565.0,\n" +
                "        \"pkgVersion\": 23.0,\n" +
                "        \"versionKey\": \"1497244825778\",\n" +
                "        \"idealScreenDensity\": \"hdpi\",\n" +
                "        \"me_averageRating\": 0.0,\n" +
                "        \"lastSubmittedOn\": \"2016-09-20T06:53:19.911+0000\",\n" +
                "        \"node_id\": 0.0,\n" +
                "        \"tempData\": \"13\",\n" +
                "        \"compatibilityLevel\": 1.0,\n" +
                "        \"creator\": \"Debasis Singh\",\n" +
                "        \"createdBy\": \"347\",\n" +
                "        \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:20:25.778+0000\",\n" +
                "        \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "        \"audience\": [\n" +
                "          \"Learner\"\n" +
                "        ],\n" +
                "        \"template\": \"domain_12843\",\n" +
                "        \"s3Key\": \"ecar_files/test_zoomannerssecondpart_1475067339226_do_20043382.ecar\",\n" +
                "        \"channel\": \"in.ekstep\",\n" +
                "        \"appId\": \"qa.ekstep.in\",\n" +
                "        \"me_timespentReview\": 0.0,\n" +
                "        \"me_videosCount\": 0.0,\n" +
                "        \"me_timespentDraft\": 0.0,\n" +
                "        \"me_imagesCount\": 91.0,\n" +
                "        \"me_audiosCount\": 53.0,\n" +
                "        \"contentDisposition\": \"inline\",\n" +
                "        \"contentEncoding\": \"gzip\",\n" +
                "        \"es_metadata_id\": \"do_20043382\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"facets\": [\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"grade 9\",\n" +
                "            \"count\": 5\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"kindergarten\",\n" +
                "            \"count\": 115\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"other\",\n" +
                "            \"count\": 86\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 1\",\n" +
                "            \"count\": 474\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 2\",\n" +
                "            \"count\": 105\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 3\",\n" +
                "            \"count\": 99\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 4\",\n" +
                "            \"count\": 98\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 5\",\n" +
                "            \"count\": 100\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 11\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 10\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 6\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 7\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 12\",\n" +
                "            \"count\": 4\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 8\",\n" +
                "            \"count\": 3\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"gradeLevel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"literacy\",\n" +
                "            \"count\": 179\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"numeracy\",\n" +
                "            \"count\": 45\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"domain\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"marathi\",\n" +
                "            \"count\": 28\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"tamil\",\n" +
                "            \"count\": 9\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"english\",\n" +
                "            \"count\": 483\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"hindi\",\n" +
                "            \"count\": 34\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"bengali\",\n" +
                "            \"count\": 9\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"gujarati\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"kannada\",\n" +
                "            \"count\": 5\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"nepali\",\n" +
                "            \"count\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"odia\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"assamese\",\n" +
                "            \"count\": 15\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"language\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"6-7\",\n" +
                "            \"count\": 28\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"7-8\",\n" +
                "            \"count\": 24\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"other\",\n" +
                "            \"count\": 18\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"<5\",\n" +
                "            \"count\": 29\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \">10\",\n" +
                "            \"count\": 15\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"5-6\",\n" +
                "            \"count\": 458\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"8-10\",\n" +
                "            \"count\": 22\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"ageGroup\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"story\",\n" +
                "            \"count\": 588\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"contentType\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    public static String getEmptySerachAPIResult() {
        return "{\n" +
                "  \"id\": \"ekstep.composite-search.search\",\n" +
                "  \"ver\": \"3.0\",\n" +
                "  \"ts\": \"2017-09-07T17:20:17ZZ\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"5fdff53c-76d0-4c1d-9775-31f509ac8d10\",\n" +
                "    \"msgid\": null,\n" +
                "    \"err\": null,\n" +
                "    \"status\": \"successful\",\n" +
                "    \"errmsg\": null\n" +
                "  },\n" +
                "  \"responseCode\": \"OK\",\n" +
                "  \"result\": {\n" +
                "    \"count\": 588,\n" +
                "    \"content\": [\n" +
                "     \n" +
                "    ],\n" +
                "    \"facets\": [\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"grade 9\",\n" +
                "            \"count\": 5\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"kindergarten\",\n" +
                "            \"count\": 115\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"other\",\n" +
                "            \"count\": 86\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 1\",\n" +
                "            \"count\": 474\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 2\",\n" +
                "            \"count\": 105\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 3\",\n" +
                "            \"count\": 99\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 4\",\n" +
                "            \"count\": 98\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 5\",\n" +
                "            \"count\": 100\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 11\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 10\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 6\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 7\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 12\",\n" +
                "            \"count\": 4\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"grade 8\",\n" +
                "            \"count\": 3\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"gradeLevel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"literacy\",\n" +
                "            \"count\": 179\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"numeracy\",\n" +
                "            \"count\": 45\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"domain\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"marathi\",\n" +
                "            \"count\": 28\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"tamil\",\n" +
                "            \"count\": 9\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"english\",\n" +
                "            \"count\": 483\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"hindi\",\n" +
                "            \"count\": 34\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"bengali\",\n" +
                "            \"count\": 9\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"gujarati\",\n" +
                "            \"count\": 3\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"kannada\",\n" +
                "            \"count\": 5\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"nepali\",\n" +
                "            \"count\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"odia\",\n" +
                "            \"count\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"assamese\",\n" +
                "            \"count\": 15\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"language\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"6-7\",\n" +
                "            \"count\": 28\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"7-8\",\n" +
                "            \"count\": 24\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"other\",\n" +
                "            \"count\": 18\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"<5\",\n" +
                "            \"count\": 29\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \">10\",\n" +
                "            \"count\": 15\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"5-6\",\n" +
                "            \"count\": 458\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"8-10\",\n" +
                "            \"count\": 22\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"ageGroup\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"name\": \"story\",\n" +
                "            \"count\": 588\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"contentType\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    public static String getSearchResultForDownload() {
        return "{\n" +
                "  \"id\": \"ekstep.composite-search.search\",\n" +
                "  \"ver\": \"3.0\",\n" +
                "  \"ts\": \"2017-09-08T04:31:15ZZ\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"92704f36-a954-452f-9a2c-02e9597c71d6\",\n" +
                "    \"msgid\": null,\n" +
                "    \"err\": null,\n" +
                "    \"status\": \"successful\",\n" +
                "    \"errmsg\": null\n" +
                "  },\n" +
                "  \"responseCode\": \"OK\",\n" +
                "  \"result\": {\n" +
                "    \"count\": 1,\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"identifier\": \"do_30019820\",\n" +
                "        \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30019820_1467120369735.ecar\",\n" +
                "        \"mimeType\": \"application/vnd.ekstep.content-collection\",\n" +
                "        \"objectType\": \"Content\",\n" +
                "        \"es_metadata_id\": \"do_30019820\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    public static String getContentListingAPIResponse() {
        return "{\n" +
                "  \"id\": \"ekstep.genie.content.home\",\n" +
                "  \"ver\": \"1.0\",\n" +
                "  \"ts\": \"2017-09-08T05:22:15+00:00\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"ef334316da79d18928fbc168d43f1491e541e0d2\",\n" +
                "    \"msgid\": \"\",\n" +
                "    \"status\": \"successful\",\n" +
                "    \"err\": \"\",\n" +
                "    \"errmsg\": \"\"\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"page\": {\n" +
                "      \"id\": \"org.ekstep.genie.content.home\",\n" +
                "      \"banners\": null,\n" +
                "      \"sections\": [\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Recommended\",\n" +
                "              \"hn\": \"सलाह\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": null,\n" +
                "          \"recommend\": {\n" +
                "            \"context\": {\n" +
                "              \"contentid\": \"\",\n" +
                "              \"did\": \"0b6db2c8e059cbe44ecb36d03c2c2908537eb019\",\n" +
                "              \"uid\": \"f3456746-e23d-495f-8b35-3674355be2a8\"\n" +
                "            },\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": null,\n" +
                "            \"filters\": null,\n" +
                "            \"facets\": null\n" +
                "          },\n" +
                "          \"contents\": null,\n" +
                "          \"resmsgid\": \"e7105789-b7b9-4624-91a3-47e8d881a2ca\",\n" +
                "          \"apiid\": \"ekstep.analytics.recommendations\",\n" +
                "          \"filterModifiable\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Collections\",\n" +
                "              \"hn\": \"सबसे लोकप्रिय\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Collection\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "              \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "              \"IL_UNIQUE_ID\": \"do_2122432102803783681180\",\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-08T15:10:29.648+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"\\u003c5\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432102803783681180/artifact/eff35f43d89c00c409aae3dcbeed0c96_1494533052420.thumb.jpeg\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.6216\",\n" +
                "              \"collections\": [\n" +
                "                \"do_212270426641924096119\",\n" +
                "                \"do_21226836960507494418\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Collection\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"705\",\n" +
                "              \"createdFor\": [\n" +
                "                \"\"\n" +
                "              ],\n" +
                "              \"createdOn\": \"2017-05-12T11:53:21.644+0000\",\n" +
                "              \"creator\": \"Debesh Rout\",\n" +
                "              \"description\": \"Alphabets in Tamil for kids\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018449_do_2122432102803783681180_2.0.ecar\",\n" +
                "              \"es_metadata_id\": \"do_2122432102803783681180\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_2122432102803783681180\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedBy\": \"582\",\n" +
                "              \"lastPublishedOn\": \"2017-05-15T07:23:38.447+0000\",\n" +
                "              \"lastUpdatedBy\": \"582\",\n" +
                "              \"lastUpdatedOn\": \"2017-06-04T16:20:12.017+0000\",\n" +
                "              \"me_averageRating\": 5,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDownloads\": 74,\n" +
                "              \"me_totalRatings\": 1,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"mediaType\": \"collection\",\n" +
                "              \"medium\": \"Tamil\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.content-collection\",\n" +
                "              \"name\": \"தமிழ் எழுத்துக்கள் / Tamil alphabets\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"organization\": [\n" +
                "                \"\"\n" +
                "              ],\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debesh\",\n" +
                "              \"pkgVersion\": 2,\n" +
                "              \"portalOwner\": \"705\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432147653017601185/artifact/eff35f43d89c00c409aae3dcbeed0c96_1494533052420.jpeg\",\n" +
                "              \"prevState\": \"Review\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018449_do_2122432102803783681180_2.0.ecar\",\n" +
                "              \"screenshots\": [\n" +
                "                \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432134616023041184/artifact/tamil4_705_1494532917_1494532893575.jpg\"\n" +
                "              ],\n" +
                "              \"size\": 72779,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"subject\": \"Tamil\",\n" +
                "              \"template\": \"\",\n" +
                "              \"usesContent\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018577_do_2122432102803783681180_2.0_spine.ecar\\\",\\\"size\\\":72781.0}}\",\n" +
                "              \"versionKey\": \"1496934629648\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:25:57.356+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_20051999/artifact/10_1466502411254.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.collection.2850\",\n" +
                "              \"collections\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Collection\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"239\",\n" +
                "              \"createdOn\": \"2016-11-06T05:44:10.869+0000\",\n" +
                "              \"creator\": \"External Testing vendor\",\n" +
                "              \"description\": \"test\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_20051999/new-6-nov_1478411092834_do_20051999_1.0.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20051999\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 1\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20051999\",\n" +
                "              \"language\": [\n" +
                "                \"Marathi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-11-06T05:44:52.965+0000\",\n" +
                "              \"lastUpdatedBy\": \"239\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:08:55.110+0000\",\n" +
                "              \"mediaType\": \"collection\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.content-collection\",\n" +
                "              \"name\": \"new 6 nov\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 46030,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"External Testing vendor\",\n" +
                "              \"pkgVersion\": 1,\n" +
                "              \"portalOwner\": \"239\",\n" +
                "              \"posterImage\": \"https://qa.ekstep.in/assets/public/content/10_1466502411254.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_20051999/new-6-nov_1478411092834_do_20051999_1.0.ecar\",\n" +
                "              \"size\": 116757,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497245157356\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"5b3a32b5-4198-4c05-b93c-681415c2d0bf\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Worksheets\",\n" +
                "              \"hn\": \"सबसे नए\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"lastPublishedOn\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Worksheet\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:12:10.904+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1467721835076_do_30030595.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.numeracy.worksheet.674\",\n" +
                "              \"collections\": [\n" +
                "                \"do_2123165037383761921419\",\n" +
                "                \"do_2123213167671296001794\",\n" +
                "                \"do_2123213574735462401828\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"Num:C2:SC3:M8\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Worksheet\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"286\",\n" +
                "              \"createdOn\": \"2016-07-01T11:50:16.641+0000\",\n" +
                "              \"creator\": \"Harish S C\",\n" +
                "              \"description\": \"Pratham Barahkadi wifi123\",\n" +
                "              \"domain\": [\n" +
                "                \"numeracy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30030595_1467721847723.ecar\",\n" +
                "              \"es_metadata_id\": \"do_30030595\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_30030595\",\n" +
                "              \"imageCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"language\": [\n" +
                "                \"Hindi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-07-05T12:30:48.536+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-07-01T11:57:41.796+0000\",\n" +
                "              \"lastUpdatedBy\": \"286\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:04:07.699+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 0,\n" +
                "              \"me_averageInteractionsPerMin\": 22.37,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 3.27,\n" +
                "              \"me_averageTimespentPerSession\": 135.9,\n" +
                "              \"me_imagesCount\": 26,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 22,\n" +
                "              \"me_totalDownloads\": 2,\n" +
                "              \"me_totalInteractions\": 3648,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 72,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 9784.73,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"PC Barahkhadi\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"optStatus\": \"Complete\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Harish S C\",\n" +
                "              \"pkgVersion\": 7,\n" +
                "              \"popularity\": 8329.56,\n" +
                "              \"portalOwner\": \"286\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_30030595_1467721847723.ecar\",\n" +
                "              \"size\": 6.962254e+06,\n" +
                "              \"soundCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497244330904\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:06:37.987+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"6-7\",\n" +
                "                \"7-8\",\n" +
                "                \"8-10\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/division_1466401982132.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1467630869246_do_30013040.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.numeracy.worksheet.469\",\n" +
                "              \"collections\": [\n" +
                "                \"do_2122981517191495681172\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Worksheet\",\n" +
                "              \"copyright\": \"CC0\",\n" +
                "              \"createdBy\": \"334\",\n" +
                "              \"createdOn\": \"2016-06-20T05:49:54.016+0000\",\n" +
                "              \"creator\": \"Parabal Partap Singh\",\n" +
                "              \"description\": \"इस वर्कशीट में विभाजन के 10 सवाल हैं। यह उन बच्चों के लिए है जो अभी विभाजन सीख रहे हैं। हर सवाल के सही या गलत होने की जानकारी दी जाएगी।\",\n" +
                "              \"domain\": [\n" +
                "                \"numeracy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30013040_1467630869906.ecar\",\n" +
                "              \"es_metadata_id\": \"do_30013040\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_30013040\",\n" +
                "              \"imageCredits\": [\n" +
                "                \"ekstep\",\n" +
                "                \"Parabal Singh\",\n" +
                "                \"EkStep\"\n" +
                "              ],\n" +
                "              \"keywords\": [\n" +
                "                \"vibhajan\",\n" +
                "                \"ek ank ka vibhajan\",\n" +
                "                \"ek ank bhag\",\n" +
                "                \"bhag\",\n" +
                "                \"divide\",\n" +
                "                \"division\"\n" +
                "              ],\n" +
                "              \"language\": [\n" +
                "                \"Hindi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-07-04T11:14:30.202+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-07-04T10:04:56.258+0000\",\n" +
                "              \"lastUpdatedBy\": \"237\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:03:44.302+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_averageInteractionsPerMin\": 33.57,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 3.29,\n" +
                "              \"me_averageTimespentPerSession\": 29.76,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 7,\n" +
                "              \"me_totalDownloads\": 7,\n" +
                "              \"me_totalInteractions\": 383,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 23,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 684.58,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"एक-अंक का विभाजन (Division)\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"optStatus\": \"Complete\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Parabal Partap Singh\",\n" +
                "              \"pkgVersion\": 2,\n" +
                "              \"popularity\": 666.35,\n" +
                "              \"portalOwner\": \"334\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_30013040_1467630869906.ecar\",\n" +
                "              \"size\": 1.492166e+06,\n" +
                "              \"soundCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"source\": \"EkStep\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"tags\": [\n" +
                "                \"ek ank ka vibhajan\",\n" +
                "                \"vibhajan\",\n" +
                "                \"division\",\n" +
                "                \"divide\",\n" +
                "                \"bhag\",\n" +
                "                \"ek ank bhag\"\n" +
                "              ],\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497243997987\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"c43dc349-f57a-4705-a289-3fa470149500\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Stories\",\n" +
                "              \"hn\": \"लोकप्रिय कहानियां\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Story\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:20:24.849+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/adjective_347_1468924253_1468924296671.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/part3_347_1474371142_1474371299864.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.1295\",\n" +
                "              \"collaborators\": [\n" +
                "                \"400\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Story\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"347\",\n" +
                "              \"createdOn\": \"2016-07-19T10:30:39.961+0000\",\n" +
                "              \"creator\": \"Debasis Singh\",\n" +
                "              \"description\": \"test_eslpart3\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_eslpart3_1474371437276_do_20043457.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20043457\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20043457\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-09-20T11:37:19.038+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-09-20T11:35:53.355+0000\",\n" +
                "              \"lastUpdatedBy\": \"331\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:05:29.917+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 4,\n" +
                "              \"me_averageInteractionsPerMin\": 33.1,\n" +
                "              \"me_averageRating\": 3,\n" +
                "              \"me_averageSessionsPerDevice\": 6.6,\n" +
                "              \"me_averageTimespentPerSession\": 115.32,\n" +
                "              \"me_imagesCount\": 65,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 10,\n" +
                "              \"me_totalDownloads\": 34,\n" +
                "              \"me_totalInteractions\": 4199,\n" +
                "              \"me_totalRatings\": 3,\n" +
                "              \"me_totalSessionsCount\": 66,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 7611.39,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"test_eslpart3\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 41576,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debasis Singh\",\n" +
                "              \"pkgVersion\": 17,\n" +
                "              \"popularity\": 7611.39,\n" +
                "              \"portalOwner\": \"347\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/adjective_347_1468924253_1468924296671.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/test_eslpart3_1474371437276_do_20043457.ecar\",\n" +
                "              \"size\": 1.2355598e+07,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"tempData\": \"08\",\n" +
                "              \"template\": \"do_20043409\",\n" +
                "              \"versionKey\": \"1497244824849\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:20:24.319+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/icon-sound_347_1468562860_1468562895967.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/bearandelephant-english_347_1473831900_1473832046025.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.1207\",\n" +
                "              \"collaborators\": [\n" +
                "                \"400\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Story\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"347\",\n" +
                "              \"createdOn\": \"2016-07-15T06:06:38.762+0000\",\n" +
                "              \"creator\": \"Debasis Singh\",\n" +
                "              \"description\": \"test_hathibhaluenglish\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_hathibhaluenglish_1473832150511_do_20043351.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20043351\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20043351\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-09-14T05:49:13.014+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-09-14T05:48:02.603+0000\",\n" +
                "              \"lastUpdatedBy\": \"200\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:05:26.686+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 8,\n" +
                "              \"me_averageInteractionsPerMin\": 25.19,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 9.8,\n" +
                "              \"me_averageTimespentPerSession\": 119.67,\n" +
                "              \"me_imagesCount\": 80,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 5,\n" +
                "              \"me_totalDownloads\": 19,\n" +
                "              \"me_totalInteractions\": 2462,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 49,\n" +
                "              \"me_totalSideloads\": 2,\n" +
                "              \"me_totalTimespent\": 5863.84,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"test_hathibhaluenglish\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 41470,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debasis Singh\",\n" +
                "              \"pkgVersion\": 26,\n" +
                "              \"popularity\": 5863.84,\n" +
                "              \"portalOwner\": \"347\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/icon-sound_347_1468562860_1468562895967.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/test_hathibhaluenglish_1473832150511_do_20043351.ecar\",\n" +
                "              \"size\": 1.8728695e+07,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"domain_12843\",\n" +
                "              \"versionKey\": \"1497244824319\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"346ee3cd-d91b-4ed4-9443-c3f84b2fa1e0\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Activities\",\n" +
                "              \"hn\": \"लोकप्रिय कहानियां\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Game\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-08T14:44:16.193+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/games/1448441481821_FinalIcon.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"authoringScore\": 10,\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.delta\",\n" +
                "              \"collections\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"communication_scheme\": \"FILE\",\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Game\",\n" +
                "              \"createdBy\": \"EkStep\",\n" +
                "              \"createdOn\": \"2016-08-18T07:42:46.034+0000\",\n" +
                "              \"creator\": \"Ekstep\",\n" +
                "              \"description\": \"Place Value Addition Subtraction Game\",\n" +
                "              \"developer\": \"Filament Games\",\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/org_ekstep_delta_16092016.ecar\",\n" +
                "              \"es_metadata_id\": \"org.ekstep.delta\",\n" +
                "              \"genieScore\": 10,\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 1\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"org.ekstep.delta\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishDate\": \"2016-05-13T13:36:10.281+0000\",\n" +
                "              \"lastPublishedOn\": \"2016-05-11T08:04:39.404+0000\",\n" +
                "              \"lastUpdatedOn\": \"2017-06-04T16:17:51.469+0000\",\n" +
                "              \"launchUrl\": \"org.ekstep.delta\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_averageInteractionsPerMin\": 18.62,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 5.02,\n" +
                "              \"me_averageTimespentPerSession\": 351.13,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 338,\n" +
                "              \"me_totalDownloads\": 37,\n" +
                "              \"me_totalInteractions\": 184793,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 1696,\n" +
                "              \"me_totalSideloads\": 5,\n" +
                "              \"me_totalTimespent\": 595514,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.android.package-archive\",\n" +
                "              \"name\": \"Take Off\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.delta\",\n" +
                "              \"owner\": \"EkStep\",\n" +
                "              \"pkgVersion\": 5,\n" +
                "              \"popularity\": 587897,\n" +
                "              \"portalOwner\": \"EkStep\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/games/1452332896911_promo_180x120.jpg\",\n" +
                "              \"publisher\": \"EkStep\",\n" +
                "              \"s3Key\": \"ecar_files/org.ekstep.takeoff_1463146569898.ecar\",\n" +
                "              \"size\": 3.4851095e+07,\n" +
                "              \"source\": \"EkStep\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"subject\": \"numeracy\",\n" +
                "              \"tempData\": \"15\",\n" +
                "              \"versionKey\": \"1496933056193\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"cd0d7d7a-c0d3-4c07-a695-944e543a029b\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }


}
