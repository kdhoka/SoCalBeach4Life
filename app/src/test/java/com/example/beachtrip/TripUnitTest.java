package com.example.beachtrip;


import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripUnitTest {

    @Test
    public void BeachObjectGettersTest(){
        LatLng dummyLL = new LatLng(0,0);
        ArrayList<ParkingLot> dummyPList = new ArrayList<>();
        ArrayList<Review> dummyRList = new ArrayList<>();
        Beach b = new Beach("dummyId","dummy",dummyLL,"10-10",dummyPList,dummyRList);
        Assert.assertEquals(b.getID(),"dummyId");
        Assert.assertEquals(b.getName(),"dummy");
        Assert.assertEquals(b.getLocation(),dummyLL);
        Assert.assertEquals(b.getHours(), "10-10");
        Assert.assertEquals(b.getParkingLots(), dummyPList);
        Assert.assertEquals(b.getReviews(), dummyRList);
    }

    @Test
    public void ParkingLotObjectGettersTest(){
        ParkingLot p = new ParkingLot("dummyId","name",null);
        Assert.assertEquals(p.getId(),"dummyId");
        Assert.assertEquals(p.getName(),"name");
        Assert.assertEquals(p.getLocation(),null);
    }

    @Test
    public void TripObjectGettersTest(){
        Trip t = new Trip("user","origin","dest",0,"0 mins","walking");
        Assert.assertEquals(t.getUser(), "user");
        Assert.assertEquals(t.getOrigin_name(), "origin");
        Assert.assertEquals(t.getDest_name(), "dest");
        Assert.assertEquals(t.getStart_time(), 0);
        Assert.assertEquals(t.getETA(), "0 mins");
        Assert.assertEquals(t.getMode(), "walking");
    }

    @Test
    public void BeachObjectEmptyConstructionTest() {
        Beach b = new Beach();
        Assert.assertEquals(b.getID(),"");
        Assert.assertEquals(b.getName(),"Null Beach");
        Assert.assertEquals(b.getLocation(),null);
        Assert.assertEquals(b.getHours(), "0-0");
        LatLng dummyLL = new LatLng(0,0);
    }

    @Test
    public void ParkingLotObjectEmptyConstructionTest() {
        ParkingLot p = new ParkingLot();
        Assert.assertEquals(p.getId(),"");
        Assert.assertEquals(p.getName(),"null parking lot");
        Assert.assertEquals(p.getLocation(),null);
    }

    @Test
    public void TripObjectEmptyConstructionTest() {
        Trip t = new Trip();
        Assert.assertEquals(t.getUser(), "");
        Assert.assertEquals(t.getOrigin_name(), "");
        Assert.assertEquals(t.getDest_name(), "");
        Assert.assertEquals(t.getStart_time(), 0);
        Assert.assertEquals(t.getETA(), "");
        Assert.assertEquals(t.getMode(), "");
    }

    @Test
    public void TripObjectSettersTest() {
        Trip t = new Trip();
        t.setUser("user");
        t.setOrigin_name("origin");
        t.setDest_name("dest");
        t.setStart_time(10);
        t.setETA("0 mins");
        t.setMode("walking");
        Assert.assertEquals(t.getUser(), "user");
        Assert.assertEquals(t.getOrigin_name(), "origin");
        Assert.assertEquals(t.getDest_name(), "dest");
        Assert.assertEquals(t.getStart_time(), 10);
        Assert.assertEquals(t.getETA(), "0 mins");
        Assert.assertEquals(t.getMode(), "walking");
    }

    @Test
    public void BeachObjectToStringTest() {
        LatLng dummyLL = new LatLng(0,0);
        Beach b = new Beach("dummyId","dummy",dummyLL,"10-10",null,null);
        Assert.assertEquals(b.toString(),"{ID: dummyId, NAME: dummy, LOCATION: lat/lng: (0.0,0.0)}");
    }

    @Test
    public void TripObjectToStringTest() {
        Trip t = new Trip("user","origin","dest",0,"0 mins","walking");
        Assert.assertEquals(t.toString(),"Origin: origin\nDestination: dest\nStart Time: Wed Dec 31 16:00:00 PST 1969\nEnd Time: Wed Dec 31 16:00:00 PST 1969\nETA: 0 mins\nMode: walking");
    }

    @Test
    public void PolylineTest() throws JSONException {
        String json  = "{\n" +
                "   \"geocoded_waypoints\" : [\n" +
                "      {\n" +
                "         \"geocoder_status\" : \"OK\",\n" +
                "         \"place_id\" : \"ChIJRTsLh0K4woARdw3qNLCQ0e0\",\n" +
                "         \"types\" : [ \"premise\" ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"geocoder_status\" : \"OK\",\n" +
                "         \"place_id\" : \"ChIJ0bGElMM33YARnu6bELordX4\",\n" +
                "         \"types\" : [ \"route\" ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"routes\" : [\n" +
                "      {\n" +
                "         \"bounds\" : {\n" +
                "            \"northeast\" : {\n" +
                "               \"lat\" : 34.0380091,\n" +
                "               \"lng\" : -118.2744741\n" +
                "            },\n" +
                "            \"southwest\" : {\n" +
                "               \"lat\" : 33.712859,\n" +
                "               \"lng\" : -118.323654\n" +
                "            }\n" +
                "         },\n" +
                "         \"copyrights\" : \"Map data ©2022\",\n" +
                "         \"legs\" : [\n" +
                "            {\n" +
                "               \"distance\" : {\n" +
                "                  \"text\" : \"27.1 mi\",\n" +
                "                  \"value\" : 43617\n" +
                "               },\n" +
                "               \"duration\" : {\n" +
                "                  \"text\" : \"36 mins\",\n" +
                "                  \"value\" : 2170\n" +
                "               },\n" +
                "               \"end_address\" : \"Unnamed Road, San Pedro, CA 90731, USA\",\n" +
                "               \"end_location\" : {\n" +
                "                  \"lat\" : 33.7134016,\n" +
                "                  \"lng\" : -118.2839421\n" +
                "               },\n" +
                "               \"start_address\" : \"2400 6th Ave, Los Angeles, CA 90018, USA\",\n" +
                "               \"start_location\" : {\n" +
                "                  \"lat\" : 34.0354704,\n" +
                "                  \"lng\" : -118.3236475\n" +
                "               },\n" +
                "               \"steps\" : [\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 mi\",\n" +
                "                        \"value\" : 279\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 38\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0379839,\n" +
                "                        \"lng\" : -118.323654\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Head \\u003cb\\u003enorth\\u003c/b\\u003e on \\u003cb\\u003e6th Ave\\u003c/b\\u003e toward \\u003cb\\u003eW 23rd St\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"upvnExaeqUc@?W?Y?c@?A?k@?uB?gA?oC?\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0354704,\n" +
                "                        \"lng\" : -118.3236475\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 mi\",\n" +
                "                        \"value\" : 547\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 90\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0380046,\n" +
                "                        \"lng\" : -118.3177134\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e at the 2nd cross street onto \\u003cb\\u003eW 21st St\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"k`wnExaeqUAgD?cA?mFAyE?S?wB?uBAuB?I?}A@O\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0379839,\n" +
                "                        \"lng\" : -118.323654\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 mi\",\n" +
                "                        \"value\" : 188\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 47\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0363165,\n" +
                "                        \"lng\" : -118.3177029\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eArlington Ave\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"o`wnEt|cqU|@?T?P?^AR?P?F@lAAl@?^?\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0380046,\n" +
                "                        \"lng\" : -118.3177134\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.2 mi\",\n" +
                "                        \"value\" : 381\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 41\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0368249,\n" +
                "                        \"lng\" : -118.3136182\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto the \\u003cb\\u003eI-10 E\\u003c/b\\u003e ramp\",\n" +
                "                     \"maneuver\" : \"ramp-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"_vvnEr|cqUKsAMuA?EC]MqA?AEi@Cc@Ca@Ek@AG?KEy@AMEu@AKAGAG?ECKEaBAMAaA\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0363165,\n" +
                "                        \"lng\" : -118.3177029\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 mi\",\n" +
                "                        \"value\" : 218\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 10\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0369253,\n" +
                "                        \"lng\" : -118.3112581\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e at the fork to continue toward \\u003cb\\u003eI-110 S\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"cyvnEbccqUA]Ck@CuAC_C?SGcD\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0368249,\n" +
                "                        \"lng\" : -118.3136182\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.5 mi\",\n" +
                "                        \"value\" : 751\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 34\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0367655,\n" +
                "                        \"lng\" : -118.3031264\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e at the fork, follow signs for \\u003cb\\u003eNormandie Ave\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"yyvnEjtbqUBKBM@E?C?[BiB?G@[?U@_@?_A?m@CwD?C?y@Am@?{@Au@?i@?]@}@?A?G?_@?A?K@]?u@?I?}B@g@?C?q@?sA?O@W?W?CB}@Be@@a@B{@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0369253,\n" +
                "                        \"lng\" : -118.3112581\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 mi\",\n" +
                "                        \"value\" : 543\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 27\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0367576,\n" +
                "                        \"lng\" : -118.2972371\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e at the fork to continue toward \\u003cb\\u003eI-110 S\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"yxvnEpaaqU@uA?A?W?Q?GAoA?YAi@?_@AsAAqA?iA?E@qA@cE?o@?o@@uC?g@@W?Y\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0367655,\n" +
                "                        \"lng\" : -118.3031264\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.6 mi\",\n" +
                "                        \"value\" : 969\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 51\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0366295,\n" +
                "                        \"lng\" : -118.286725\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e at the fork, follow signs for \\u003cb\\u003eHoover St\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"wxvnEv|_qUB]BS?IBkA?K@a@?G?k@?_B?eB?m@AaC?_@?_AAS?}@?k@@gB?U?C?gC@c@?A?I?Y@sDBaK@mB@]@gA?oA?q@?Y\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0367576,\n" +
                "                        \"lng\" : -118.2972371\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.4 mi\",\n" +
                "                        \"value\" : 693\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 49\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 34.0377189,\n" +
                "                        \"lng\" : -118.2793726\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eleft\\u003c/b\\u003e at the fork to continue toward \\u003cb\\u003eI-110 S\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"}wvnE~z}pU?U?a@?]?CAoAA_@Aa@Ac@AS?ICg@?EEu@I}ACSAW?ACU?AMQAY?OAMA[?GA[CY?EAYAE?CCYCOAMEWCSGYAGE[I_@G]G[G[G_@AIESG]AGEWEY?CEY?CE]?AEY?CC_@C_@Ee@AQ?AAYAGAO\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0366295,\n" +
                "                        \"lng\" : -118.286725\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"20.4 mi\",\n" +
                "                        \"value\" : 32794\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"20 mins\",\n" +
                "                        \"value\" : 1205\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7530641,\n" +
                "                        \"lng\" : -118.2912333\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e at the fork, follow signs for \\u003cb\\u003eSan Pedro\\u003c/b\\u003e and merge onto \\u003cb\\u003eI-110 S\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"fork-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"w~vnE`m|pULi@?C?AA{@?[?Q?E?Y?Y?Y@W?Q?C@U@[@k@B]@UBM@I@G@GBKBI@GBGBIBGHODKNUDGV[BEBCNQFEDGHI?APWJOHMDI@EBEDOBG?A@IBIHSDMDMBGHQ@C@ALODELM@?DCPIDAPGF?RAD?P?XB`@@b@Bt@BH@Z@|@Fn@DB?v@BlA@TAr@AZCD?b@C`@C\\\\CZC@?VAJAbAEHQL?XA^APA\\\\AbBGdBE\\\\AXAZAXAXAXAZAV?Z?V?F?R?B?T@XBD?bBNVDlAVVHLDHDVJJDLDVLVLTLVLl@\\\\XNn@^VLTNTLn@\\\\RJxDvBj@XTLXNPJB@TLVJLF`@PXLPFFBRHBBPFXHVJVHXJVJVLTJJDLHVLTLTLFDZPRJXPTLTLB@RLVNVLTNXNBBNHVLVNTL@BTJVNVNTLTLXPTLTLPJFBTLLHHBTJTJZLb@PVHRHRFLDd@L\\\\JPD@@TDZHVFTDB@XDXFVDXD`@F^DNBF?J@LBF?P@XBTBZ@N@F@P?H@V@T?J@^?H?T@T?L?L?V?rB@jA?`@?Z@b@?\\\\?p@?@?b@?`@?V?PAJ?H?P?N?~@?n@Az@?l@?xA?nAAfC?x@AD?`B?jB?pAAhD?|@AnD?dAAzCA`B?|BEx@AlACt@C@?bCKTAdFSf@Cb@ATAH?D?|@Ex@Al@A`@A^AD?v@ER?p@Er@C`BEFAnACVA^?R?^A^?z@AjAAhC?jABfABL?V@`@@n@@n@@X@L?~ABp@@L@f@@F?R@^?R@L?r@@jA?|@APAXALCT?VAh@?X?XAf@?d@Ab@?N?t@AZ?b@ApBAhBAJ?VAT?N?N?XAf@?X?RAZ?Z?TAl@?R?N?JAN?`@?t@@V?D?d@@D@X?V@B?d@Bf@BT@d@BP@H@L?H@V@ZBV@fBHX@ZBV@X@X?X@Z@V?X?P?@@F?X?Z?N?N?J?H?B?F?R?`B?B?L?V?Z?X?Z?V?P?F?L?d@?R?J?|@?l@?@?dA?X?b@?N?T?N?V?n@?r@?lB@X?Z?V?R?b@?bB?x@?p@?|B?j@?d@?dA?jC?d@?T?P?H?Z?|B?l@?nA?\\\\?l@?X?r@?l@?|@?d@?^?x@?Z?R?^?r@Al@?T@J?f@?p@?R?lCBX?n@?hA@P?P?lA@J?R?\\\\?t@@l@?t@@\\\\?B?\\\\?X?bA@N?j@@jA?\\\\@pB?^?B?p@@PAJ@zAAb@?l@?ZAd@?V?jAAZ?r@?x@Ab@?p@A\\\\?V?T?v@?VAP?d@?V?\\\\A`@?B?FA\\\\?XAVARAFAZAXC@?TAXCTCZEZCFAPCTC\\\\EVCXETCHATCRC\\\\Ex@KRCVETCNCXCVETCPCLAVEVCB?VEZEh@Gj@GPCLCbD_@ZERCRCZEZEf@GXCNAHAVC`@CPAVAd@Av@AN?X?X@X?^BP?N@J@XBD?h@DZ@VBr@DJ@L@F@P@X@XBF@X@TBXBT@D?\\\\BXBD?TBR@F?V@ZBV?V@L?J@X?X?\\\\@L?J?V?R@F?N?N@~@@f@@X@D?`@?d@?Z?V@Z?X?V?X?B?T?r@@X?V?Z?n@?P@nA?nA?pB@l@?B?`A?\\\\?R@D?V?^?h@@d@@T?H?N@\\\\@V@Z@V?B@H?j@BF?TBX@F?P@ZBV@XBT@\\\\BZBVBXBXBXBp@HZBXBVBZBVBXBXBXDF?PBXBXBXBP@F@X@VBL@L@XBV@X@XBlAFX@J?P@T@Z@D?~ADB?T@~@@N@T@P?pBFV?B?V@X@Z@T?t@B\\\\?j@CR@V@`@@V?X@J@N?P@F?V@P@F?Z@V@H?V@P@P@D?Z@T@D@V@V@P@F@X@X@P@F@X@XBV@ZBr@BTBZ@@?VBN@H@XBXDZDPDD@B@JJJBZHPD^JTJB@RFVJRJXNPH\\\\RVNRNRNTPTRJHHHZXj@j@RRVXHHFDRTRRTRRRRTRPPNBBRPTRPL@@TPPNDBNLZRRNVNVNPL@?VNVNDBPHTLXLFDNFTLVJZLRHXJhA^VHXHPDFBTF^HTFXDXFXFXDVDTB^FVBL@L@XDZBT@R@f@Bn@BX@Z?J@`A?r@?X?Z?X?t@?V?\\\\?Z?V?Z?V?Z?h@?b@?Z?p@?l@?`@@\\\\?X?X?`A?J?Z?\\\\?Z?X?Z?Z?X?X?X?Z?X?N?d@@X?V?Z@X?F?j@@Z?Z?X?X@J?L?x@?fBBZ?V?Z?R@F?Z?V?n@@v@?\\\\?Z@t@?l@@^?x@@X?X?pA?T?F?T?Z?X?VAH?V?V?Z?X?f@AL?x@?\\\\?\\\\AV?r@?b@?l@A`@Ah@C\\\\CXAXCVCXCZEVC\\\\ETC\\\\Gr@Kh@Kx@MXEn@Iz@MVEr@KVCr@KZETEZCTC\\\\ETAHAPAXCXAZAX?^AvAG^?p@AtBAR?hBA@?v@?l@?hAAx@?z@AlAAzBEjCCrCCnAAhBClAA~@Ad@Aj@A\\\\Cj@C`@Cp@En@EnAKvDYvAINA`@CX?`@C^?\\\\AV?^?fB?N?xA?V?XAZ?hAAj@Ah@?~BClCCbDCfBAtAAx@AX?X?Z?zB?^?r@?`E?b@Aj@?h@?\\\\@tA@VEPEzA?J?`@?pA?\\\\?j@An@?TAT@z@?z@?xB@l@?\\\\?r@?~@?T?@?d@?p@?`A?pA?p@?N?~@?^?X?p@@p@@^Bb@?d@B@?v@BR@^@bAD\\\\@T@X@b@@`@@R?L?dBAd@AXARAXA\\\\Cn@GTAXCRA\\\\Cd@CRAn@Eh@El@ExAK@?\\\\C^CNAJAl@CVAD?T?\\\\AbAA^A`@@T?X@d@@pBDp@@lABpBFj@@hBDZ@X@H?P?bABx@Bj@@V@z@@@?Z@R?H?D@P?j@@b@?t@?nA?d@?D?z@?dC?T?pA?f@?T?fA?D?`@?P?J?D?X?X?R?F?r@?F?P?X?Z?V?Z@X?`@BP?f@BPB\\\\BN@RBh@DPBJ@\\\\D^FD?VDVDXF\\\\DvEt@lDj@tDj@PDl@Jh@H\\\\DhC`@nBZzDn@XDXDVDZD^FNBv@LVDVD\\\\FVDXDF?PB\\\\Dd@Ff@BF@L@Z@VBX@T@Z@X?^@T?D?N?D?N?J?X?f@?z@?H?pA?bA?\\\\?r@?pA?fA?v@?X?H?Z?d@?D?bA?lA?@?H?f@?H?Z?Z?\\\\?J?N?R?D?H?N?p@?X?ZAT?D?R?Z?X?V?F?R?R?J@XAV?B?V@V?Z?R?^?@?l@AD?X?V@`@?nBAbA?P?T@HAL@JAJ?\\\\@`@AF?D?x@?R?B?j@?v@?H?r@?R?^?fA?x@?jA?z@?^?l@?n@?@?N?D?D?\\\\?R?r@?~C?hE?bF?R?N?R?L?Z?b@?pA?x@?lA?`@?p@?N?T?F?B?b@?^?d@?fA?v@?n@?r@?b@?T?L?x@?j@?bA?|@?J?F?xA?ZAn@?V@@?T?\\\\?X?T?^?L?dABT@X@V@X@V@D?VBT@L@L@XBXBXDb@DVD\\\\Dp@HLBXDXDB?B@j@Hl@HnAP^FXBp@JXDXDjC^d@Fd@FZDn@JTBXDZFd@FF?HBr@JVBTDPB^DVDVDD?TDVBXBZBXB@?T@X@Z?B?V@X?XAV?ZAJAJ?DATAVCZC\\\\EVEXEVE\\\\GTGREBAVGZKTGXKZMXKTKXMXORMPIFEPKVOVQVQTQNOXSPQRSJIHKTUPQPQXY`@c@^_@LMHIVWTSBCROTSTQVQLIBCZQPKXQTMBCRMVMTMXQLGHGTMb@WfBeAf@Wn@_@VQVMVODCh@[RMXOVOp@a@@ALGXORMVMTMXMVMTKZMTKDCPGBAXKTKZIVIRI\\\\IBAVIVGREXIVGB?TEVG\\\\GXEVENCFAXCVEFATAXE^CTCXCzBQz@GhE_@J?HADATAXCXCTCHAt@GdCSvAMr@GhBObAIdAI`CSrBQfBOXCRAXCZCXANCF?XC\\\\ATCXAh@CHAZAXAXA\\\\AVAZAVAZA`BG^At@CXAp@CZAXAVAXAB?nAEr@C`DMZAXAd@ALAt@CZAXC\\\\AR?XALAf@AXAd@AD?F?^?T@\\\\?Z@R@^BVBh@Fb@Df@FTFVDTDXFVFVFTFD@XJVH@@THVHXLTHVJNFHBVJXJTJXJPFJDLFB@RHVH^NZJXJVJTJ\\\\LRFVJXJZNRFXJTJZJTJVJHBNFPFDBVJXHFDNFVHVJB@RHD@PHB?@@PFDBRFDBPFB@RHB@l@TDBPFB@RHB@RHDBVLPHB@PJDBPHBBRJBB^T\\\\V@@B@PNTPRPTRTTFF\\\\\\\\BBLPBBNNBDLNBDLNDDLPBDJPBDLPBDLP?@BBJR@@@Bl@dABBJRPXNVNVLVBDLR?@NTBDHNNTDHXd@BDBBJPBDJNBB@BRVJNBFJL@@BDJLFFBDFHDFJJFFLPBBDDHJBBNNBBNNBDNNBBNLDDLLDDNLBBPLBBLJBBBBLJB@BBNLB@PLDBNJDDB@LHBBJFB@PJBBFDFDFBLHFBNH@@FBNHDBNFBBB@RHBBD@HDD@VJVJVJDBPFXHVJD?B@NFVFXHD@XDTDXDTF@?XDF@JBb@FTBZD@?TB@?XB^BVBT@\\\\@V@F?^?Z@F?PAN?L?L?V?@?\\\\AH?`@AXA\\\\A\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 34.0377189,\n" +
                "                        \"lng\" : -118.2793726\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.5 mi\",\n" +
                "                        \"value\" : 732\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 35\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7485042,\n" +
                "                        \"lng\" : -118.2883086\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Take exit \\u003cb\\u003e1A\\u003c/b\\u003e for \\u003cb\\u003eCA-47\\u003c/b\\u003e toward \\u003cb\\u003eVincent Thomas Bridge\\u003c/b\\u003e/\\u003cwbr/\\u003e\\u003cb\\u003eTerminal Island\\u003c/b\\u003e/\\u003cwbr/\\u003e\\u003cb\\u003eLong Beach\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"ramp-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"sk_mEdw~pURLB@L?L?V@J?X@\\\\?d@BF?b@@T@P?P@J?\\\\@B?T@L@H@XBL@T@XDVBH?J@D@L@N@TBP?LALAJAJABAPCBA@?HCHCBA@AJCFC@AHE@AFC@AHGLGFGLKPQPUR[HKBEDMFMDMFMDMBMDO@I@ABQBSBO@K@O?O@Y?K?EAW?c@?KA]?C?OAoA?E?O\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7530641,\n" +
                "                        \"lng\" : -118.2912333\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 mi\",\n" +
                "                        \"value\" : 201\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 9\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7485678,\n" +
                "                        \"lng\" : -118.2861385\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Continue onto \\u003cb\\u003eCA-47 N\\u003c/b\\u003e/\\u003cwbr/\\u003e\\u003cb\\u003eSeaside Fwy\\u003c/b\\u003e\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"co~lE|d~pUAy@Ay@CqC?QA_ACyA\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7485042,\n" +
                "                        \"lng\" : -118.2883086\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.3 mi\",\n" +
                "                        \"value\" : 543\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 42\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7485503,\n" +
                "                        \"lng\" : -118.2805083\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Take exit \\u003cb\\u003e1C\\u003c/b\\u003e for \\u003cb\\u003eHarbor Blvd\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"ramp-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"qo~lEjw}pUFY@U?q@@iA?_@?]?m@?K@K?M@O@I?IBMBSDQBMHWDKDQZcAHU@GBIBOBM?ABI?K@M@M?M?KAEASCOAMIa@AEE]I]Ii@EK[aBCOGYSsA?A\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7485678,\n" +
                "                        \"lng\" : -118.2861385\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.8 mi\",\n" +
                "                        \"value\" : 1352\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 125\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.736599,\n" +
                "                        \"lng\" : -118.279452\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eN Harbor Blvd\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"mo~lEdt|pUZObAa@NGXMf@UHCJENG@ANGJEBAHCTGBATELCF?RCRAb@?j@?|@?zB@rB?nA?f@?B?rD?dA?lB@vD?l@?f@@\\\\@D?D?N@j@BNDz@HT@`@DB?t@Bl@?lA?N?T?\\\\@`A?r@?D?J?F?VC^AB?DAB?BAB?VG\\\\GXK^O@?^U^S\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7485503,\n" +
                "                        \"lng\" : -118.2805083\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.8 mi\",\n" +
                "                        \"value\" : 1215\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 93\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7261395,\n" +
                "                        \"lng\" : -118.2783079\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eMiner St\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"wd|lEpm|pUZT\\\\VFDFDJDLNPJB?D@B@F@B@JDJ?H?F?~@?x@?dC@f@ArD?`@BNAl@@fA?R?d@?l@?n@?d@?xC@xB?V?N?HAD?HAHALANEF@B?B?B?BALCNGXSLMPOXWJKRSHIJKFG@A@ABCBCFEHEBCHEFCDCDABCJCPGFAVI@?FCXIDADAFAB?DAJADAD?ZCP?FAB?B?PC@AJABABAXG@Ah@Ob@K|@W\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.736599,\n" +
                "                        \"lng\" : -118.279452\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.4 mi\",\n" +
                "                        \"value\" : 602\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 48\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7244981,\n" +
                "                        \"lng\" : -118.284511\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eE 22nd St\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"kczlElf|pUd@jC@JX~AF`@FVDVHd@Lv@BHd@tCb@bCFZXfBRdALr@BNBRLn@F`@@HX|A@FHf@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7261395,\n" +
                "                        \"lng\" : -118.2783079\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.5 mi\",\n" +
                "                        \"value\" : 806\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 66\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7178733,\n" +
                "                        \"lng\" : -118.2842173\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eVia Cabrillo-Marina\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"cyylEdm}pUDR`@EbAOlBW`@GHAbAMTEXEVEZIVGVILEDAHERIbAe@ZOTKNGJEHEh@UJG^OTKNINGNGNENCPAPALAJ@L?F@NBPBHBF@LDDBRHPLFDLLPRTXZ`@NPFJBBBDLLFDFFHBJFJDJBNBN@P?D?D?\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7244981,\n" +
                "                        \"lng\" : -118.284511\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.4 mi\",\n" +
                "                        \"value\" : 602\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"2 mins\",\n" +
                "                        \"value\" : 101\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7128646,\n" +
                "                        \"lng\" : -118.2854953\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eShoshonean Rd\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-right\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"uoxlEjk}pUBT?DBF@B@FBBBDDDFFNHXTt@f@XPRLJFFDJDHDLDJDJB`@HPDXFj@Jx@N\\\\FVDRB@?LBJ@F?B@L?N@N?L?N?hAEz@CF?t@C~@EFABABABADEDAFAHCFAH?J?b@@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7178733,\n" +
                "                        \"lng\" : -118.2842173\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"92 ft\",\n" +
                "                        \"value\" : 28\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 10\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.712859,\n" +
                "                        \"lng\" : -118.2851883\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"kpwlEjs}pU?}@\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.7128646,\n" +
                "                        \"lng\" : -118.2854953\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"distance\" : {\n" +
                "                        \"text\" : \"0.1 mi\",\n" +
                "                        \"value\" : 173\n" +
                "                     },\n" +
                "                     \"duration\" : {\n" +
                "                        \"text\" : \"1 min\",\n" +
                "                        \"value\" : 49\n" +
                "                     },\n" +
                "                     \"end_location\" : {\n" +
                "                        \"lat\" : 33.7134016,\n" +
                "                        \"lng\" : -118.2839421\n" +
                "                     },\n" +
                "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e\",\n" +
                "                     \"maneuver\" : \"turn-left\",\n" +
                "                     \"polyline\" : {\n" +
                "                        \"points\" : \"kpwlElq}pU}@?c@?A?A?A??A?AAI?k@?y@A_A?eA\"\n" +
                "                     },\n" +
                "                     \"start_location\" : {\n" +
                "                        \"lat\" : 33.712859,\n" +
                "                        \"lng\" : -118.2851883\n" +
                "                     },\n" +
                "                     \"travel_mode\" : \"DRIVING\"\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"traffic_speed_entry\" : [],\n" +
                "               \"via_waypoint\" : []\n" +
                "            }\n" +
                "         ],\n" +
                "         \"overview_polyline\" : {\n" +
                "            \"points\" : \"upvnExaeqU}G?wE?AkFAsQAsI@O|@?f@?lA?zBA^?KsAM{AQoBMqBYqEK{BIyCOmKHc@FmIG}MFkPNuHEyHBmTLmF?kN?gMLg`@IuF]eG?AMQAYA]Ac@GuAEc@i@gDq@yD]mCS_DJ_AAoBDmDJmBHe@Lc@b@y@lAwAf@w@Pe@^mAP_@b@c@n@Wr@AhH^~CBxBIdEUHQf@ArDMbHSzCDhBNdB\\\\fA`@~DpB|KfGbD~AnCdAhDxAhDlBnHdExCbBtDvAnD~@`En@zBRhCJ`PDnUGx[GfJKpEQnMc@fHUdIOfCCtEBnCFnINnABjE?nAGtBChJGrMI|DBpEPnFVnEJrH@|d@@lX?zTBvPF~JD~HAlQIxCItDYrJkAdOgBzDc@bBKfCC`DLdEXdIf@pJL|`@NbIZvLbAnIn@xPd@pADjABhACdBDfADnCHpCL~If@xAPVFNLx@RlA`@vAp@|AbAtDlDjChCzBjBhCbBnCvA|Ap@tC~@rDx@zC^~ALlCH~G@nF?~N@fLFrPHdN@pGC|CGhBM|Fw@vImArD_@rHS~ME~UW~EG|CQnKu@lBI~IAfSQbNEdIAfA@lBClBEl@?jEA|K@tJ?pHFjGTfFBlBG|BQlIi@fDQvCEvBDtO\\\\rHNbDDzM?hK?dEHxD^fg@~HfFv@dEj@|DRfC@lT?dV?vmAAfU?hE?lFL|CVpEj@`TvCfEl@xDb@|CHxCMjBU~A[lA]dBq@bDiBrCcClDoDdA_ApAaApCaBpGsDjKcGtCoApDkAlDu@xC_@fTgBx^wCrNk@rTu@pEOvDBdCTjBZxCx@tHtChStHvEhB`Bz@bCbBzBzBjAxAbDpFtC~E`C~CzC~CjCpBtCbBdAd@vCdAtCn@zCd@~CRrC@hDGd@NjABtDHvBL`CRbA?l@Ij@Qv@c@x@{@f@{@h@aBLaABeAC_CK}LCyAFY@gA@uDHwAVkAp@wBPgABu@Gi@SsAu@uD_@_ChCgAnBy@x@S|@IxN@xSDhE\\\\`EBhD@~AG|@QzAq@^SZTd@\\\\r@f@TDNFT?fG@lG?lMBjDAf@E^Cd@MrAiAt@u@h@c@fAa@rA]rAKhASlCu@hAxGrBpLxApId@pCNz@dBU|Eo@bB[bAY|BeAfDyA~@_@`AIp@Fp@Nj@Z|@`Av@bA`@`@l@Tv@DHTBLJTfD|Bj@VjAXxEx@bBDbEMnAKVKf@Eb@@?}@}@?i@?Ay@A_E\"\n" +
                "         },\n" +
                "         \"summary\" : \"I-110 S\",\n" +
                "         \"warnings\" : [],\n" +
                "         \"waypoint_order\" : []\n" +
                "      }\n" +
                "   ],\n" +
                "   \"status\" : \"OK\"\n" +
                "}";
        DirectionsJSONParser parser = new DirectionsJSONParser();
        List<List<HashMap<String, String>>> routes = parser.parse(new JSONObject(json));
        Assert.assertNotEquals(routes, null);
    }
}
