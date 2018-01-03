package com.yinker.tinyv.verfication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yinker.tinyv.vo.ComplexNetworkInput;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PojoVerifier {

    /**
     * Verify ComplexNetworkInput
     *
     * @param in
     * @return
     */
    public static VerifyResult verify(ComplexNetworkInput in) {
        if (in.getCalls() == null)
            return new VerifyResult(false, "'calls' field is required.");
        if (in.getMiguan() == null || in.getMiguan().equals("")) {
            in.setMiguan(Integer.MAX_VALUE + "");
            return new VerifyResult(true, "ok");
        }
        if (in.getTongd() == null || in.getMiguan().equals(""))
            return new VerifyResult(true, "ok");
        return new VerifyResult(true, "OK");
    }

}
