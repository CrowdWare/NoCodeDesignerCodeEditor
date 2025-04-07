# NoCodeDesigner CodeEditor

> A fork of [`darkrockstudios/texteditor`](https://github.com/darkrockstudios/texteditor), adapted and maintained by [CrowdWare](https://crowdware.info)

![License](https://img.shields.io/badge/license-MIT%20%2B%20GPL3-blue.svg)
![badge-jvm]

---

## About

This is a stripped-down version of the original Compose Text Editor, focused **entirely on Desktop (JVM)**.  
All Android components have been removed for better compatibility in **pure Compose Desktop projects**.

It also includes a critical fix to prevent crashes when the cursor is placed beyond the bounds of the text layout.

---

## Based on

Original Project:  
👉 [darkrockstudios/texteditor](https://github.com/Wavesonics/ComposeTextEditorLibrary)  
MIT-licensed by **Adam Brown**

Modifications and Desktop-only version maintained by **CrowdWare**  
GPL-3 licensed additions

---

## Why?

The original project is an impressive attempt at re-implementing a rich text editor from scratch to overcome the limitations of `BasicTextField` in Compose.

This fork was created to:

- Use the editor **independently of Android**
- Avoid dependency issues related to AGP and Android Studio versions
- Provide better stability and control in Desktop-only Compose environments

---

## Why “NoCode” if there’s code?
NoCodeDesigner empowers users to build apps without writing code – but for advanced use cases, 
we provide a fallback: a clean and structured editor for our own markup language (SML). 
Think of it as LowCode under a NoCode umbrella — enabling power users without losing 
simplicity for beginners.

---

## What’s Working

- Rich text rendering & editing
- Cursor movement, selection, copy/cut/paste
- Efficient layout: only visible lines are rendered
- Scroll handling
- Supports custom span rendering (e.g., spell check underlines)
- Emits fine-grained edit events for real-time processing

---

## Platforms

✅ Supported: **JVM/Desktop**  
❌ Removed: Android, Web, WASM (to avoid AGP and Gradle plugin issues)

---

## Want to try it?

This project is **not published to Maven**. To try it locally:

### 🛠️ Local Setup (No Maven required)

1. **Clone this repo** next to your app:

```
your-workspace/
├── your-app/
└── texteditor-desktop/
```

2. **Include it in your `settings.gradle.kts` of `your-app`:**

```kotlin
includeBuild("../texteditor-desktop")
```

3. **Add dependency in `your-app/build.gradle.kts`:**

```kotlin
dependencies {
    implementation("com.crowdware.texteditor:texteditor-core")
}
```

You're done! Now you can use the text editor in your Compose Desktop project without any Android dependencies.

---

## License

This repository includes MIT-licensed code by Adam Brown (2024–2025).  
All additions and modifications by **CrowdWare** are licensed under **GPL-3.0**.

See [LICENSE](./LICENSE) and [LICENSE.GPL3](./LICENSE.GPL3) for details.

---

## Credits

- 🧑‍💻 Original author: [Adam Brown](https://github.com/darkrockstudios)
- 🛠️ Maintained & adapted by: [CrowdWare](https://crowdware.info)

---

[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat
