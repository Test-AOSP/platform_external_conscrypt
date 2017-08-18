/*
 * Copyright (C) 2010 The Android Open Source Project
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

package libcore.javax.net.ssl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.security.KeyStore.Builder;
import java.security.KeyStore.PasswordProtection;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.KeyStoreBuilderParameters;
import libcore.java.security.TestKeyStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KeyStoreBuilderParametersTest extends AbstractSSLTest {

    @Test
    public void test_init_Builder() {
        TestKeyStore testKeyStore = TestKeyStore.getClient();
        Builder builder = Builder.newInstance(
                testKeyStore.keyStore, new PasswordProtection(testKeyStore.storePassword));
        KeyStoreBuilderParameters ksbp = new KeyStoreBuilderParameters(builder);
        assertNotNull(ksbp);
        assertNotNull(ksbp.getParameters());
        assertEquals(1, ksbp.getParameters().size());
        assertSame(builder, ksbp.getParameters().get(0));
    }

    @Test
    public void test_init_List_null() {
        try {
            new KeyStoreBuilderParameters((List<Builder>) null);
            fail();
        } catch (NullPointerException expected) {
            // Ignored.
        }
    }

    @Test
    public void test_init_List() {
        TestKeyStore testKeyStore1 = TestKeyStore.getClient();
        TestKeyStore testKeyStore2 = TestKeyStore.getServer();
        Builder builder1 = Builder.newInstance(
                testKeyStore1.keyStore, new PasswordProtection(testKeyStore1.storePassword));
        Builder builder2 = Builder.newInstance(
                testKeyStore2.keyStore, new PasswordProtection(testKeyStore2.storePassword));

        List<Builder> list = Arrays.asList(builder1, builder2);
        KeyStoreBuilderParameters ksbp = new KeyStoreBuilderParameters(list);
        assertNotNull(ksbp);
        assertNotNull(ksbp.getParameters());
        assertNotSame(list, ksbp.getParameters());
        assertEquals(2, ksbp.getParameters().size());
        assertSame(builder1, ksbp.getParameters().get(0));
        assertSame(builder2, ksbp.getParameters().get(1));

        // confirm result is not modifiable
        try {
            ksbp.getParameters().set(0, builder2);
            fail();
        } catch (UnsupportedOperationException expected) {
            // Ignored.
        }

        // confirm result is a copy of original
        list.set(0, builder2);
        assertSame(builder1, ksbp.getParameters().get(0));
    }
}
