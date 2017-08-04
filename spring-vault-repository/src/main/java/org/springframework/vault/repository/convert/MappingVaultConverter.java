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

import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;
import org.springframework.vault.repository.mapping.VaultPersistentEntity;
import org.springframework.vault.repository.mapping.VaultPersistentProperty;

/**
 * @author Mark Paluch
 */
public class MappingVaultConverter extends AbstractVaultConverter {

	private final MappingContext<? extends VaultPersistentEntity<?>, VaultPersistentProperty> mappingContext;

	private VaultTypeMapper typeMapper;

	public MappingVaultConverter(
			MappingContext<? extends VaultPersistentEntity<?>, VaultPersistentProperty> mappingContext) {

		super(new DefaultConversionService());

		Assert.notNull(mappingContext, "MappingContext must not be null");

		this.mappingContext = mappingContext;
		this.typeMapper = new DefaultVaultTypeMapper(
				DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, mappingContext);
	}

	/**
	 * Configures the {@link VaultTypeMapper} to be used to add type information to
	 * {@link SecretDocument}s created by the converter and how to lookup type information
	 * from {@link SecretDocument}s when reading them. Uses a
	 * {@link DefaultVaultTypeMapper} by default. Setting this to {@literal null} will
	 * reset the {@link org.springframework.data.convert.TypeMapper} to the default one.
	 *
	 * @param typeMapper the typeMapper to set, must not be {@literal null}.
	 */
	public void setTypeMapper(VaultTypeMapper typeMapper) {

		Assert.notNull(typeMapper, "VaultTypeMapper must not be null");
		this.typeMapper = typeMapper;
	}

	@Override
	public MappingContext<? extends VaultPersistentEntity<?>, VaultPersistentProperty> getMappingContext() {
		return mappingContext;
	}

	@Override
	public <R> R read(Class<R> type, SecretDocument source) {
		return null;
	}

	@Override
	public void write(Object source, SecretDocument sink) {

	}
}
