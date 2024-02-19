# DragonFly BTA Model Library
<p align="center">
  <img width="256" height="209" src="https://github.com/UselessBullets/DragonFly/assets/80850784/646a4d77-06c1-40cb-a070-c86dcfda55c1">
</p>

### Currently Available Features
- ModelHelper, Helper class for creating block and entity models
- Supports a limited version of Modern MC Models
- Supports a limited version of Modern Bedrock edition entity Models

See DragonFly Example Mod [Here](https://github.com/UselessBullets/DragonFlyExample)

### Add to project
- Add version variable to "gradle.properties" `dragonfly_version=1.4.4-7.1`
- Add mod repository to "build.gradle"
```
ivy {
	url = "https://github.com/UselessSolutions"
	patternLayout {
		artifact "[organisation]/releases/download/v[revision]/[module]-[revision].jar"
		m2compatible = true
	}
	metadataSources { artifact() }
}
```
- Implement mod in "build.gradle" `modImplementation "DragonFly:dragonfly:${project.dragonfly_version}"`

### Credits:
- UselessBullets - Block Model Support
- baguchan - Entity Model Support
- 0999312 [MMLib](https://github.com/0999312/MMLib/tree/3e87210c9305a5724e06c492be503533a1ebcd59) (Tremendously helpful for Entity Model support)
