package org.rapidoid.http;

/*
 * #%L
 * rapidoid-http-fast
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.lambda.Mapper;
import org.rapidoid.u.U;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class PathPattern {

	private static final String PATH_PARAM_REGEX = "\\{([^\\}]+)\\}";

	private static final Pattern PATH_PARAM_PARTS = Pattern.compile("(\\w+)(?::(.+))?");

	private static final String DEFAULT_GROUP_REGEX = "[^/]+";

	private final String path;

	private final Pattern pattern;

	private final Map<String, String> groups;

	public PathPattern(String path, Pattern pattern, Map<String, String> groups) {
		this.path = path;
		this.pattern = pattern;
		this.groups = groups;
	}

	public static PathPattern from(String path) {
		final Map<String, String> groups = U.map();
		final AtomicInteger counter = new AtomicInteger();

		String regex = U.replace(path, PATH_PARAM_REGEX, new Mapper<String[], String>() {
			@Override
			public String map(String[] gr) throws Exception {
				String group = gr[1];

				Matcher m = PATH_PARAM_PARTS.matcher(group);
				U.must(m.matches(), "Invalid path parameter, expected {var} or {var:regex} syntax!");

				String name = m.group(1);
				String regex = U.or(m.group(2), DEFAULT_GROUP_REGEX);

				String groupId = "g" + counter.incrementAndGet();

				U.must(!groups.containsKey(name), "Cannot have multiple path parameters with the same name: '%s'", name);
				groups.put(name, groupId);

				return "(?<" + groupId + ">" + regex.replaceAll("\\\\", "\\\\\\\\") + ")";
			}
		});

		if (regex.endsWith("/*")) {
			regex = U.sub(regex, 0, -1) + ".*";
		}

		Pattern pattern = Pattern.compile(regex);
		return new PathPattern(path, pattern, groups);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PathPattern that = (PathPattern) o;

		return path.equals(that.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	public String getPath() {
		return path;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Map<String, String> match(String path) {
		Matcher matcher = pattern.matcher(path);
		boolean matches = matcher.matches();
		Map<String, String> params = null;

		if (matches) {
			params = U.map();

			for (Map.Entry<String, String> e : groups.entrySet()) {
				params.put(e.getKey(), matcher.group(e.getValue()));
			}
		}

		return params;
	}

	@Override
	public String toString() {
		return "PathPattern{" +
				"path='" + path + '\'' +
				", pattern=" + pattern +
				", groups=" + groups +
				'}';
	}
}
