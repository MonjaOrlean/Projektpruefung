use std::fs::{self, OpenOptions};
use std::path::PathBuf;
use std::process::{Command, Stdio};
use tauri::Manager;

fn windows_pfad_ohne_verbatim(pfad: PathBuf) -> PathBuf {
  let text = pfad.to_string_lossy();

  if let Some(rest) = text.strip_prefix(r"\\?\") {
    PathBuf::from(rest)
  } else {
    pfad
  }
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
  tauri::Builder::default()
    .plugin(tauri_plugin_log::Builder::new().build())
    .setup(|app| {
      let resource_dir = app.path().resource_dir()?;

      let backend_dir = resource_dir
        .join("resources")
        .join("backend");

      let java_pfad = windows_pfad_ohne_verbatim(
        backend_dir
          .join("runtime")
          .join("bin")
          .join("java.exe"),
      );

      let backend_jar = windows_pfad_ohne_verbatim(
        backend_dir.join("backend.jar"),
      );

      let daten_dir = windows_pfad_ohne_verbatim(
        app.path().app_data_dir()?,
      );

      fs::create_dir_all(&daten_dir)?;

      let log_pfad = daten_dir.join("backend.log");

      let log_datei = OpenOptions::new()
        .create(true)
        .append(true)
        .open(&log_pfad)?;

      let fehler_log = log_datei.try_clone()?;

      println!("Java-Pfad: {:?}", java_pfad);
      println!("Backend-JAR: {:?}", backend_jar);
      println!("Datenordner: {:?}", daten_dir);
      println!("Backend-Log: {:?}", log_pfad);

      #[cfg(target_os = "windows")]
      {
        use std::os::windows::process::CommandExt;

        const CREATE_NO_WINDOW: u32 = 0x08000000;

        let child = Command::new(&java_pfad)
          .arg("-jar")
          .arg(&backend_jar)
          .current_dir(&daten_dir)
          .stdout(Stdio::from(log_datei))
          .stderr(Stdio::from(fehler_log))
          .creation_flags(CREATE_NO_WINDOW)
          .spawn()?;

        println!("Backend gestartet. PID: {}", child.id());
      }

      Ok(())
    })
    .run(tauri::generate_context!())
    .expect("Fehler beim Starten der Tauri-Anwendung");
}
