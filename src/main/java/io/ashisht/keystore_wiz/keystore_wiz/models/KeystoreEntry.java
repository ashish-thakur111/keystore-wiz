/**
 * Copyright ©️ 2025 Ashish Thakur <ashish.thakur1110@gmail.com>
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
package io.ashisht.keystore_wiz.keystore_wiz.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KeystoreEntry {
    private String alias;
    private String issuer;
    private String subject;
    private String startDate;
    private String endDate;
}
