/*
 * Copyright 2016 the original author or authors.
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
package org.springframework.vault.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * Value object to bind Vault HTTP Transit Key Config API requests.
 *
 * @author Mark Paluch
 */
public class VaultTransitKeyConfiguration {

	@JsonProperty("deletion_allowed")
	@Nullable
	private final Boolean deletionAllowed;

	@JsonProperty("latest_version")
	@Nullable
	private final Integer latestVersion;

	@JsonProperty("min_decryption_version")
	@Nullable
	private final Integer minDecryptionVersion;

	@JsonProperty("min_encryption_version")
	@Nullable
	private final Integer minEncryptionVersion;

	private VaultTransitKeyConfiguration(@Nullable Boolean deletionAllowed,
										 @Nullable Integer latestVersion,
										 @Nullable Integer minDecryptionVersion,
										 @Nullable Integer minEncryptionVersion) {

		this.deletionAllowed = deletionAllowed;
		this.latestVersion = latestVersion;
		this.minDecryptionVersion = minDecryptionVersion;
		this.minEncryptionVersion = minEncryptionVersion;
	}

	/**
	 * @return a new {@link VaultTransitKeyConfigurationBuilder}.
	 */
	public static VaultTransitKeyConfigurationBuilder builder() {
		return new VaultTransitKeyConfigurationBuilder();
	}

	/**
	 * @return whether key deletion is configured
	 */
	@Nullable
	public Boolean getDeletionAllowed() {
		return deletionAllowed;
	}

	/**
	 * @return latest key version
	 */
	@Nullable
	public Integer getLatestVersion() {
		return latestVersion;
	}

	/**
	 * @return minimum version of the key that can be used to decrypt
	 */
	@Nullable
	public Integer getMinDecryptionVersion() {
		return minDecryptionVersion;
	}

	/**
	 * @return minimum version of the key that can be used to encrypt
	 */
	@Nullable
	public Integer getMinEncryptionVersion() {
		return minEncryptionVersion;
	}

	/**
	 * Builder for {@link VaultTransitKeyConfiguration}.
	 */
	public static class VaultTransitKeyConfigurationBuilder {

		@Nullable
		private Boolean deletionAllowed;

		@Nullable
		private Integer latestVersion;

		@Nullable
		private Integer minDecryptionVersion;

		@Nullable
		private Integer minEncryptionVersion;

		VaultTransitKeyConfigurationBuilder() {
		}

		/**
		 * Set whether key deletion is allowed.
		 *
		 * @param deletionAllowed {@literal true} if key deletion should be allowed.
		 * @return {@code this} {@link VaultTransitKeyConfigurationBuilder}.
		 */
		public VaultTransitKeyConfigurationBuilder deletionAllowed(boolean deletionAllowed) {
			this.deletionAllowed = deletionAllowed;
			return this;
		}

		/**
		 * Set the latest key version.
		 *
		 * @param latestVersion key version.
		 * @return {@code this} {@link VaultTransitKeyConfigurationBuilder}.
		 */
		public VaultTransitKeyConfigurationBuilder latestVersion(int latestVersion) {
			this.latestVersion = latestVersion;
			return this;
		}

		/**
		 * Set the minimum version of the key that can be used to decrypt.
		 *
		 * @param minDecryptionVersion key version.
		 * @return {@code this} {@link VaultTransitKeyConfigurationBuilder}.
		 */
		public VaultTransitKeyConfigurationBuilder minDecryptionVersion(int minDecryptionVersion) {
			this.minDecryptionVersion = minDecryptionVersion;
			return this;
		}

		/**
		 * Set the minimum version of the key that can be used to encrypt.
		 *
		 * @param minEncryptionVersion key version.
		 * @return {@code this} {@link VaultTransitKeyConfigurationBuilder}.
		 */
		public VaultTransitKeyConfigurationBuilder minEncryptionVersion(int minEncryptionVersion) {
			this.minEncryptionVersion = minEncryptionVersion;
			return this;
		}

		/**
		 * Build a new {@link VaultTransitKeyConfiguration} instance.
		 *
		 * @return a new {@link VaultTransitKeyConfiguration}.
		 */
		public VaultTransitKeyConfiguration build() {
			return new VaultTransitKeyConfiguration(deletionAllowed, latestVersion, minDecryptionVersion, minEncryptionVersion);
		}
	}
}
