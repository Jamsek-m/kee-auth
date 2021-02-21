/*
 *  Copyright (c) 2019-2021 Miha Jamsek and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mjamsek.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebKey {
    
    @JsonProperty("kid")
    private String kid;
    
    @JsonProperty("alg")
    private String alg;
    
    @JsonProperty("n")
    private String n;
    
    @JsonProperty("e")
    private String e;
    
    @JsonProperty("crv")
    private String crv;
    
    @JsonProperty("x")
    private String x;
    
    @JsonProperty("y")
    private String y;
    
    @JsonIgnore
    private String secret;
    
    @JsonProperty("x5c")
    private List<String> x5c;
    
    public String getKid() {
        return kid;
    }
    
    public void setKid(String kid) {
        this.kid = kid;
    }
    
    public String getAlg() {
        return alg;
    }
    
    public void setAlg(String alg) {
        this.alg = alg;
    }
    
    public String getN() {
        return n;
    }
    
    public void setN(String n) {
        this.n = n;
    }
    
    public String getE() {
        return e;
    }
    
    public void setE(String e) {
        this.e = e;
    }
    
    public String getCrv() {
        return crv;
    }
    
    public void setCrv(String crv) {
        this.crv = crv;
    }
    
    public String getX() {
        return x;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    public String getY() {
        return y;
    }
    
    public void setY(String y) {
        this.y = y;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public List<String> getX5c() {
        return x5c;
    }
    
    public void setX5c(List<String> x5c) {
        this.x5c = x5c;
    }
    
}
