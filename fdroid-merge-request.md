# F-Droid Merge Request Template

## Título:
```
New app: Audio Output Switcher
```

## Descrição:
```markdown
## Required
* [x] The app complies with the [inclusion criteria](https://f-droid.org/docs/Inclusion_Policy)
* [x] The original app author has been notified (and does not oppose the inclusion) <!--I am the original author-->
* [x] All related [fdroiddata](https://gitlab.com/fdroid/fdroiddata/issues) and [RFP issues](https://gitlab.com/fdroid/rfp/issues) have been referenced in this merge request
* [ ] Builds with `fdroid build` and all pipelines pass <!--Will be tested by CI-->

## Strongly Recommended
* [x] The upstream app source code repo contains the app metadata _(summary/description/images/changelog/etc)_ in a [Fastlane](https://gitlab.com/snippets/1895688) or [Triple-T](https://gitlab.com/snippets/1901490) folder structure
* [x] Releases are tagged

## Suggested
* [x] External repos are added as git submodules instead of srclibs <!--Not applicable - no external dependencies-->
* [x] Enable [Reproducible Builds](https://f-droid.org/docs/Reproducible_Builds) <!--App is configured for reproducible builds-->
* [ ] Multiple apks for native code <!--Not applicable - no native code-->

---------------------

Quick Settings tile for switching audio output devices.

Adds a tile to Quick Settings that provides instant access to Android's native audio output selection dialog. Perfect for users who frequently switch between headphones, speakers, and Bluetooth devices.

**Features:**
- Quick access to audio output selector
- Integration with native Android system
- Support for all connected audio devices
- Lightweight with no background battery usage
- Material Design interface

**Technical details:**
- Requires Android 15+ (API 35)
- MIT License
- No proprietary dependencies
- Gradle wrapper security issues fixed
- Fastlane metadata included

Closes rfp#3321

/label ~"New App"
```

## Próximos passos:
1. Criar o merge request com título e descrição acima
2. Aguardar pipelines rodarem
3. Responder comentários se houver
4. Aguardar aprovação dos mantenedores