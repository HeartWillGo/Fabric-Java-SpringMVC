package com.heartgo.controller;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;

import static java.lang.String.format;

public class SetUp {
    public String ADMIN_NAME;
    public String USER_NAME;
    private Config config;
    public SetUp(Config config,String ADMIN_NAME,String USER_NAME){
        this.ADMIN_NAME=ADMIN_NAME;
        this.USER_NAME=USER_NAME;
        this.config=config;
    }

    public  HFClient SetupClient(){
        HFClient client = HFClient.createNewInstance();
        try {

            System.out.println("client:" + client.toString());
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

          //  System.out.println("client CryptoSuite:" + client.getCryptoSuite());
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
    public void InitUsers(Collection<SampleOrg> sampleOrgs){

        File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        //  System.out.println("pathname :"+System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        if (sampleStoreFile.exists()) { //For testing start fresh
            System.out.println("sample exit: Yes");
            sampleStoreFile.delete();
        }

        final SampleStore sampleStore = new SampleStore(sampleStoreFile);
        //  sampleStoreFile.deleteOnExit();

        //SampleUser can be any implementation that implements org.User Interface

        ////////////////////////////
        // get users for all orgs
        System.out.println("testSampleOrgs.size :"+sampleOrgs.size());
        try {
            for (SampleOrg sampleOrg : sampleOrgs) {

                HFCAClient ca = sampleOrg.getCAClient();

                final String orgName = sampleOrg.getName();
                final String mspid = sampleOrg.getMSPID();
                ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
                System.out.println("orgName:" + orgName);
                System.out.println("mspid:" + mspid);
                HFCAInfo info = ca.info(); //just check if we connect at all.
                System.out.println("info tostring:" + info.toString());
                //  assertNotNull(info);
                String infoName = info.getCAName();
                System.out.println("infoName:" + infoName);
//                if (infoName != null && !infoName.isEmpty()) {
//                    assertEquals(ca.getCAName(), infoName);
//                }

                System.out.println("TEST_ADMIN_NAME:" + ADMIN_NAME);

                SampleUser admin = sampleStore.getMember(ADMIN_NAME, orgName);
                if (!admin.isEnrolled()) {  //Preregistered admin only needs to be enrolled with Fabric caClient.
                    System.out.println("!admin.isEnrolled()");
                    admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
                    admin.setMspId(mspid);
                }

                sampleOrg.setAdmin(admin); // The admin of this org --

                SampleUser user = sampleStore.getMember(USER_NAME, sampleOrg.getName());
                if (!user.isRegistered()) {  // users need to be registered AND enrolled
                    System.out.println("!user.isRegistered()");
                    RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");
                    user.setEnrollmentSecret(ca.register(rr, admin));
                }
                if (!user.isEnrolled()) {
                    System.out.println("!user.isEnrolled()");
                    user.setEnrollment(ca.enroll(user.getName(), user.getEnrollmentSecret()));
                    user.setMspId(mspid);
                }
                sampleOrg.addUser(user); //Remember user belongs to this Org

                final String sampleOrgName = sampleOrg.getName();
                final String sampleOrgDomainName = sampleOrg.getDomainName();
                //     System.out.println("sampleOrgName:"+sampleOrgName+"  sampleOrgDomainName :"+sampleOrgDomainName);

                // src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/
//                 System.out.println("keystore:"+Paths.get(config.getTestChannelPath(), "crypto-config\\peerOrganizations",
//                         sampleOrgDomainName, format("users\\Admin@%s\\msp\\keystore", sampleOrgDomainName)).toFile());
//                 System.out.println("keyStore2:"+Paths.get(config.getTestChannelPath(), "crypto-config\\peerOrganizations", sampleOrgDomainName,
//                         format("users\\Admin@%s\\msp\\signcerts\\Admin@%s-cert.pem", sampleOrgDomainName, sampleOrgDomainName)).toFile());
                String str1=config.getTestChannelPath()+"/crypto-config/peerOrganizations/"+sampleOrgDomainName+format("/users/Admin@%s/msp/keystore", sampleOrgDomainName);
                String str2=config.getTestChannelPath()+"/crypto-config/peerOrganizations/"+sampleOrgDomainName+ format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName, sampleOrgDomainName);
                File file=new File(str1);
                File file2=new File(str2);

                System.out.println("file :"+file+" "+file.exists());
                System.out.println("file2 :"+file2+" "+file2.exists());
                System.out.println("file :"+str1 );
                System.out.println("file2 :"+str2 );
                SampleUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg.getMSPID(),
                        Util.findFileSk(file),
                        file2);

                sampleOrg.setPeerAdmin(peerOrgAdmin); //A special user that can create channels, join peers and install chaincode
                System.out.println("peerOrgAdmin:" + peerOrgAdmin.mspId + "  " + peerOrgAdmin.getName() + "  " + peerOrgAdmin.toString());
            }
        }catch (Exception e){
            e.printStackTrace();

        }


    }
    public void SetupUsers(Collection<SampleOrg> sampleOrgs){
        File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        final SampleStore sampleStore = new SampleStore(sampleStoreFile);

        //SampleUser can be any implementation that implements org.User Interface

        ////////////////////////////
        // get users for all orgs

        for (SampleOrg sampleOrg : sampleOrgs) {

            final String orgName = sampleOrg.getName();

            SampleUser admin = sampleStore.getMember(ADMIN_NAME, orgName);
            sampleOrg.setAdmin(admin); // The admin of this org.

            // No need to enroll or register all done in End2endIt !
            SampleUser user = sampleStore.getMember(USER_NAME, orgName);
            sampleOrg.addUser(user);  //Remember user belongs to this Org

            sampleOrg.setPeerAdmin(sampleStore.getMember(orgName + "Admin", orgName));
        }

    }
}
