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

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import org.springframework.data.convert.ConfigurableTypeInformationMapper;
import org.springframework.data.convert.SimpleTypeInformationMapper;
import org.springframework.data.util.TypeInformation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultVaultTypeMapper}.
 *
 * @author Mark Paluch
 */
public class DefaultVaultTypeMapperUnitTests {

	ConfigurableTypeInformationMapper configurableTypeInformationMapper = new ConfigurableTypeInformationMapper(
			Collections.singletonMap(String.class, "1"));
	SimpleTypeInformationMapper simpleTypeInformationMapper = new SimpleTypeInformationMapper();
	DefaultVaultTypeMapper typeMapper = new DefaultVaultTypeMapper();

	@Test
	public void defaultInstanceWritesClasses() {

		writesTypeToField(new SecretDocument(), String.class, String.class.getName());
	}

	@Test
	public void defaultInstanceReadsClasses() {

		SecretDocument document = new SecretDocument();
		document.put(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, String.class.getName());

		readsTypeFromField(document, String.class);
	}

	@Test
	public void writesMapKeyForType() {

		typeMapper = new DefaultVaultTypeMapper(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY,
				Collections.singletonList(configurableTypeInformationMapper));

		writesTypeToField(new SecretDocument(), String.class, "1");
		writesTypeToField(new SecretDocument(), Object.class, null);
	}

	@Test
	public void writesClassNamesForUnmappedValuesIfConfigured() {

		typeMapper = new DefaultVaultTypeMapper(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY,
				Arrays.asList(configurableTypeInformationMapper,
						simpleTypeInformationMapper));
		writesTypeToField(new SecretDocument(), String.class, "1");
		writesTypeToField(new SecretDocument(), Object.class, Object.class.getName());
	}

	@Test
	public void readsTypeForMapKey() {

		typeMapper = new DefaultVaultTypeMapper(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY,
				Collections.singletonList(configurableTypeInformationMapper));

		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, "1")), String.class);
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, "unmapped")), null);
	}

	@Test
	public void readsTypeLoadingClassesForUnmappedTypesIfConfigured() {

		typeMapper = new DefaultVaultTypeMapper(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY,
				Arrays.asList(configurableTypeInformationMapper,
						simpleTypeInformationMapper));

		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, "1")), String.class);
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, Object.class.getName())),
				Object.class);
	}

	@Test
	public void addsFullyQualifiedClassNameUnderDefaultKeyByDefault() {
		writesTypeToField(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, new SecretDocument(),
				String.class);
	}

	@Test
	public void writesTypeToCustomFieldIfConfigured() {

		typeMapper = new DefaultVaultTypeMapper("_custom");
		writesTypeToField("_custom", new SecretDocument(), String.class);
	}

	@Test
	public void doesNotWriteTypeInformationInCaseKeyIsSetToNull() {

		typeMapper = new DefaultVaultTypeMapper(null);
		writesTypeToField(null, new SecretDocument(), String.class);
	}

	@Test
	public void readsTypeFromDefaultKeyByDefault() {
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, String.class.getName())),
				String.class);
	}

	@Test
	public void readsTypeFromCustomFieldConfigured() {

		typeMapper = new DefaultVaultTypeMapper("_custom");
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap("_custom",
						String.class.getName())), String.class);
	}

	@Test
	public void returnsNullIfNoTypeInfoInDocument() {
		readsTypeFromField(new SecretDocument(), null);
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, "")), null);
	}

	@Test
	public void returnsNullIfClassCannotBeLoaded() {
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, "fooBar")), null);
	}

	@Test
	public void returnsNullIfTypeKeySetToNull() {
		typeMapper = new DefaultVaultTypeMapper(null);
		readsTypeFromField(
				new SecretDocument(Collections.singletonMap(
						DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, String.class)), null);
	}

	@Test
	public void returnsCorrectTypeKey() {

		assertThat(typeMapper.isTypeKey(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY))
				.isTrue();

		typeMapper = new DefaultVaultTypeMapper("_custom");
		assertThat(typeMapper.isTypeKey("_custom")).isTrue();
		assertThat(typeMapper.isTypeKey(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY))
				.isFalse();

		typeMapper = new DefaultVaultTypeMapper(null);
		assertThat(typeMapper.isTypeKey("_custom")).isFalse();
		assertThat(typeMapper.isTypeKey(DefaultVaultTypeMapper.DEFAULT_TYPE_KEY))
				.isFalse();
	}

	private void readsTypeFromField(SecretDocument document, Class<?> type) {

		TypeInformation<?> typeInfo = typeMapper.readType(document);

		if (type != null) {
			assertThat(typeInfo).isNotNull();
			assertThat(typeInfo.getType()).isAssignableFrom(type);
		}
		else {
			assertThat(typeInfo).isNull();
		}
	}

	private void writesTypeToField(String field, SecretDocument document, Class<?> type) {

		typeMapper.writeType(type, document);

		if (field == null) {
			assertThat(document.keySet()).isEmpty();
		}
		else {
			assertThat(document.getBody()).containsKey(field);
			assertThat(document.getBody()).containsEntry(field, type.getName());
		}
	}

	private void writesTypeToField(SecretDocument document, Class<?> type, Object value) {

		typeMapper.writeType(type, document);

		if (value == null) {
			assertThat(document.keySet()).isEmpty();
		}
		else {
			assertThat(document.getBody()).containsKey(
					DefaultVaultTypeMapper.DEFAULT_TYPE_KEY);
			assertThat(document.getBody()).containsEntry(
					DefaultVaultTypeMapper.DEFAULT_TYPE_KEY, value);
		}
	}
}
