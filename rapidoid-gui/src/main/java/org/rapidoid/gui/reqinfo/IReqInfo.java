package org.rapidoid.gui.reqinfo;

/*
 * #%L
 * rapidoid-gui
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski and contributors
 * %%
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
 * #L%
 */

import java.util.Map;
import java.util.Set;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;

@Authors("Nikolche Mihajlovski")
@Since("5.0.4")
public interface IReqInfo {

	boolean exists();

	boolean isGetReq();

	String verb();

	String path();

	String uri();

	String host();

	Map<String, Object> data();

	Map<String, String> params();

	Map<String, Object> posted();

	Map<String, byte[]> files();

	Map<String, String> headers();

	Map<String, String> cookies();

	String username();

	Set<String> roles();

}