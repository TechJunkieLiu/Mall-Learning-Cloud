package com.aiyangniu.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取RSA公钥
 *
 * @author lzq
 * @date 2023/09/21
 */
@Api(value = "KeyPairController", tags = "获取RSA公钥")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KeyPairController {

    private final KeyPair keyPair;

    /**
     * {
     *   "keys": [
     *     {
     *       "kty": "RSA",
     *       "e": "AQAB",
     *       "n": "prBOA67SwS1PaDHvGSuQqGRBfzOTaZUtyJdacvhMdME_NJUzYXA0DtMcCk84PHZ1E_Q6VQjG4zgim3vBAaGcKHDO2c6cmh3w83rcp2eqvCEzuzcvIJiiMMiOWwdDdIbibPpITv1ZQEQyWEV38MZYvxQpdBSkgrZFfO_Za_Cs4Ok"
     *     }
     *   ]
     * }
     */
    @GetMapping("/rsa/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
