package com.pingan.respository;

import com.pingan.myutil.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.io.File;
import java.util.Collection;

public class getUsers {
    private static final ClientHelper clientHelper = new ClientHelper();
    private static final Config clientConfig = Config.getConfig();
    private static final String TEST_ADMIN_NAME = "admin";
    private static final String TESTUSER_1_NAME = "user1";

    private static final Log logger = LogFactory.getLog(SetupUsers.class);

    public void getusers() throws Exception {

        ClientConfigHelper configHelper = new ClientConfigHelper();
        configHelper.clearConfig();
        configHelper.customizeConfig();



        Collection<SampleOrg> sampleOrgs=clientConfig.getIntegrationTestsSampleOrgs();
        // Get Org1
//        SampleOrg sampleOrg = clientConfig.getIntegrationTestsSampleOrg("peerOrg1");
//        sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));

        ////////////////////////////
        // Set up USERS

        // Persistence is not part of SDK. Sample file store is for
        // demonstration purposes only!
        // MUST be replaced with more robust application implementation
        // (Database, LDAP)

        // SampleUser can be any implementation that implements
        // org.hyperledger.fabric.sdk.User Interface

        File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        final SampleStore sampleStore = new SampleStore(sampleStoreFile);

        //SampleUser can be any implementation that implements org.User Interface

        ////////////////////////////
        // get users for all orgs



        for (SampleOrg sampleOrg : sampleOrgs) {

            final String orgName = sampleOrg.getName();

            SampleUser admin = sampleStore.getMember(TEST_ADMIN_NAME, orgName);
            sampleOrg.setAdmin(admin); // The admin of this org.

            // No need to enroll or register all done in End2endIt !
            SampleUser user = sampleStore.getMember(TESTUSER_1_NAME, orgName);
            sampleOrg.addUser(user);  //Remember user belongs to this Org

            sampleOrg.setPeerAdmin(sampleStore.getMember(orgName + "Admin", orgName));
        }


    }

}
