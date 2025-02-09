/**
 * Copyright 2025 Ashish Thakur(ashish.thakur1110@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ashisht.keystore_wiz.keystore_wiz.core;

import io.ashisht.keystore_wiz.keystore_wiz.models.KeystoreEntry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Getter
@Component
@Slf4j
public class KeystoreManager {

    /**
     * -- GETTER --
     *  Returns the loaded keystore.
     *
     */
    private KeyStore keystore; // Holds the loaded keystore

    /**
     * Loads a keystore from the specified file path.
     *
     * @param filePath The path to the keystore file.
     * @param password The password to access the keystore.
     * @throws KeyStoreException        If the keystore type is invalid.
     * @throws IOException              If there is an I/O problem.
     * @throws NoSuchAlgorithmException If the algorithm for keystore integrity is not found.
     * @throws CertificateException     If any certificate in the keystore cannot be loaded.
     */
    public void loadKeystore(String filePath, String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        // Step 1: Create a KeyStore instance for the desired type (e.g., JKS)
        keystore = KeyStore.getInstance("JKS"); // Replace "JKS" with "PKCS12" if needed

        // Step 2: Load the keystore from the file
        try (FileInputStream fis = new FileInputStream(filePath)) {
            keystore.load(fis, password.toCharArray());
            log.atInfo().log("Keystore loaded successfully.");
        } catch (IOException e) {
            log.atError().log("Error loading keystore: " + e.getMessage());
            throw e;
        }
    }


    public boolean isKeystoreLoaded() {
        return keystore != null;
    }

    /**
     * Retrieves all the certificates from the loaded keystore.
     *
     * @return A list of {@link KeystoreEntry} objects, each containing the alias, issuer, subject, start date, and end date of a certificate in the keystore.
     * @throws KeyStoreException If there is a problem accessing the keystore.
     */
    public List<KeystoreEntry> getKeystoreEntries() throws KeyStoreException {
        Enumeration<String> aliases = keystore.aliases();
        List<KeystoreEntry> keystoreEntries = new ArrayList<>();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (keystore.isCertificateEntry(alias)) {
                Certificate cert = keystore.getCertificate(alias);
                X509Certificate x509Cert = (X509Certificate) cert;
                KeystoreEntry entry = KeystoreEntry.builder().alias(alias)
                        .issuer(x509Cert.getIssuerX500Principal().getName())
                        .subject(x509Cert.getSubjectX500Principal().getName())
                        .startDate(x509Cert.getNotBefore().toString())
                        .endDate(x509Cert.getNotAfter().toString()).build();
                keystoreEntries.add(entry);
            } else if (keystore.isKeyEntry(alias)) {
                log.atError().log("The alias is associated with a key entry.");
            }
        }
        return keystoreEntries;
    }

    // deleteKeystoreEntry method
    public void deleteKeystoreEntry(String alias) throws KeyStoreException {
        keystore.deleteEntry(alias);
    }
}

