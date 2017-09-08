package com.heartgo.respository;

import com.heartgo.myutil.ConfigHelper;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.net.MalformedURLException;
import java.util.Collection;

import static java.lang.String.format;

public class CheckConfig {
    private static final Config testConfig = Config.getConfig();

    private static final ConfigHelper configHelper = new ConfigHelper();

    public   Collection<SampleOrg> checkConfig(Config config) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        out("\n\n\nRUNNING: End2endIT.\n");
        configHelper.clearConfig();
        configHelper.customizeConfig();
        Collection<SampleOrg> sampleOrgs;
        sampleOrgs = config.getIntegrationTestsSampleOrgs();
        //Set up hfca for each sample org

        for (SampleOrg sampleOrg : sampleOrgs) {
            String caName = sampleOrg.getCAName(); //Try one of each name and no name.
            if (caName != null && !caName.isEmpty()) {
                sampleOrg.setCAClient(HFCAClient.createNewInstance(caName, sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
            } else {
                sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
            }
        }
        return sampleOrgs;
    }
    static void out(String format, Object... args) {

        System.err.flush();
        System.out.flush();

        System.out.println(format(format, args));
        System.err.flush();
        System.out.flush();

    }
}
