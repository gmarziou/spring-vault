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
package org.springframework.vault.repository;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.repository.VaultRepositoryIntegrationTests.VaultRepositoryTestConfiguration;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;
import org.springframework.vault.util.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Paluch
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = VaultRepositoryTestConfiguration.class)
public class VaultRepositoryIntegrationTests extends IntegrationTestSupport {

	@Configuration
	@EnableVaultRepositories(considerNestedRepositories = true)
	static class VaultRepositoryTestConfiguration extends
			VaultIntegrationTestConfiguration {
	}

	@Autowired
	VaultRepository vaultRepository;
	@Autowired
	VaultTemplate vaultTemplate;

	@Test
	public void loadAndSave() {

		Foo foo = new Foo();
		foo.setId("foo-key");
		foo.setName("bar");

		vaultRepository.save(foo);

		Iterable<Foo> all = vaultRepository.findAll();

		assertThat(all).contains(foo);
		assertThat(vaultRepository.findById("foo-key")).contains(foo);

	}

	interface VaultRepository extends CrudRepository<Foo, String> {
	}

	@Data
	static class Foo {

		String id, name;
	}
}
