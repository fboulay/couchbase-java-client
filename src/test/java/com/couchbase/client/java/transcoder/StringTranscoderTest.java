/**
 * Copyright (C) 2014 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.client.java.transcoder;

import com.couchbase.client.core.lang.Tuple2;
import com.couchbase.client.core.message.ResponseStatus;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.buffer.Unpooled;
import com.couchbase.client.deps.io.netty.util.CharsetUtil;
import com.couchbase.client.java.document.StringDocument;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringTranscoderTest {


    private StringTranscoder converter;

    @Before
    public void setup() {
        converter = new StringTranscoder();
    }

    @Test
    public void shouldEncodeString() {
        StringDocument document = StringDocument.create("id", "value");
        Tuple2<ByteBuf, Integer> encoded = converter.encode(document);

        assertEquals("value", encoded.value1().toString(CharsetUtil.UTF_8));
        assertEquals(TranscoderUtils.STRING_COMPAT_FLAGS, (long) encoded.value2());
    }

    @Test
    public void shouldDecodeCommonString() {
        ByteBuf content = Unpooled.copiedBuffer("value", CharsetUtil.UTF_8);
        StringDocument decoded = converter.decode("id", content, 0, 0, TranscoderUtils.STRING_COMMON_FLAGS,
            ResponseStatus.SUCCESS);

        assertEquals("value", decoded.content());
    }

    @Test
    public void shouldDecodeLegacyString() {
        ByteBuf content = Unpooled.copiedBuffer("value", CharsetUtil.UTF_8);
        StringDocument decoded = converter.decode("id", content, 0, 0, 0,
            ResponseStatus.SUCCESS);

        assertEquals("value", decoded.content());
    }

}
