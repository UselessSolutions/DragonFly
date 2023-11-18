# DragonFly BTA Model Library

### Currently Available Features
- ModelHelper, Helper class for creating block and entity models
- Supports a limited version of Modern MC Models
- Supports a limited version of Modern Bedrock edition entity Models

This is an early pre release version of DragonFly, expect many bugs, and expect many features to get broken in future updates

See DragonFly Example Mod [Here](https://github.com/UselessBullets/DragonFlyExample)

### Add to project
- Add version variable to "gradle.properties" `dragonfly_version=0.1.1`
- Add mod repository to "build.gradle"
```
ivy {
		url = "https://github.com/UselessBullets"
		patternLayout {
			artifact "[organisation]/releases/download/v[revision]/[module]-[revision].jar"
			m2compatible = true
		}
		metadataSources { artifact() }
	}
```
- Implement mod in "build.gradle" `modImplementation "DragonFly:dragonfly:${project.dragonfly_version}"`

Credit : 0999312 https://github.com/0999312/MMLib/tree/3e87210c9305a5724e06c492be503533a1ebcd59
