# DragonFly BTA Model Library
<p align="center">
  <img width="256" height="209" src="https://github.com/UselessBullets/DragonFly/assets/80850784/646a4d77-06c1-40cb-a070-c86dcfda55c1">
</p>

# About
DragonFly is a model library which allows other mods made for BTA to use model formats used in modern Minecraft versions. It makes modeling and texturing able to be done through programs like BlockBench making complex models much less daunting to implement.

- DragonFly also allows texturepacks to change the models of any mod using DragonFly.
- DragonFly uses HalpLibe for texture registration and for HalpLibe's entrypoints
- You can look at the [DragonFly Example Mod](https://github.com/UselessSolutions/DragonFlyExample) for help using DragonFly in your project

### Supported data types:
DragonFly Allows the use of
- Modern Java Block/Item Models
- Modern Java Block States
- Modern Bedrock Entity Geometries
- Modern Bedrock Entity Animations

# Setup
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

# Credits:
- UselessBullets - Block Model Support
- baguchan - Entity Model Support
- 0999312 [MMLib](https://github.com/0999312/MMLib/tree/3e87210c9305a5724e06c492be503533a1ebcd59) (Tremendously helpful for Entity Model support)
