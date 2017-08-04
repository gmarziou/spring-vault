/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.vault.repository.convert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.vault.support.VaultResponse;

/**
 * @author Mark Paluch
 */
public class SecretDocument {

	private @Nullable String id;
	private final Map<String, Object> body;

	public SecretDocument() {
		this(null, new LinkedHashMap<>());
	}

	public SecretDocument(Map<String, Object> body) {
		this(null, body);
	}

	public SecretDocument(@Nullable String id, Map<String, Object> body) {
		this.id = id;
		this.body = body;
	}

	public SecretDocument(String id) {
		this(id, new LinkedHashMap<>());
	}

	public static SecretDocument from(@Nullable String id, VaultResponse vaultResponse) {
		return new SecretDocument(id, vaultResponse.getData());
	}

	public Object get(String key) {
		return body.get(key);
	}

	@Nullable
	public String getId() {
		return id;
	}

	public void setId(@Nullable String id) {
		this.id = id;
	}

	public void put(String key, Object value) {
		this.body.put(key, value);
	}

	public Set<String> keySet() {
		return body.keySet();
	}

	public Map<String, Object> getBody() {
		return body;
	}
}
