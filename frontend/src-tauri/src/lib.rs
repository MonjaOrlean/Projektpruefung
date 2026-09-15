use std::fs::{self, OpenOptions};
use std::path::PathBuf;
use std::process::{Child, Command, Stdio};
use std::sync::Mutex;

use tauri::{Manager, RunEvent};

struct BackendProcess {
  child: Mutex<Option<Child>>,
}

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
  let app = tauri::Builder::default()
    .plugin(
      tauri_plugin_log::Builder::new()
        .build()
    )
    .manage(BackendProcess {
      child: Mutex::new(None),
    })
    .setup(|app| {
      let resource_dir =
        app.path().resource_dir()?;

      let backend_dir = resource_dir
        .join("resources")
        .join("backend");

      let java_pfad =
        windows_pfad_ohne_verbatim(
          backend_dir
            .join("runtime")
            .join("bin")
            .join("java.exe"),
        );

      let backend_jar =
        windows_pfad_ohne_verbatim(
          backend_dir.join("backend.jar"),
        );

      let daten_dir =
        windows_pfad_ohne_verbatim(
          app.path().app_data_dir()?,
        );

      fs::create_dir_all(
        &daten_dir
      )?;

      let log_pfad =
        daten_dir.join("backend.log");

      let log_datei =
        OpenOptions::new()
          .create(true)
          .append(true)
          .open(&log_pfad)?;

      let fehler_log =
        log_datei.try_clone()?;

      println!(
        "Java-Pfad: {:?}",
        java_pfad
      );

      println!(
        "Backend-JAR: {:?}",
        backend_jar
      );

      println!(
        "Datenordner: {:?}",
        daten_dir
      );

      println!(
        "Backend-Log: {:?}",
        log_pfad
      );

      #[cfg(target_os = "windows")]
      {
        use std::os::windows::process::CommandExt;

        const CREATE_NO_WINDOW: u32 =
          0x08000000;

        let child =
          Command::new(&java_pfad)
            .arg("-jar")
            .arg(&backend_jar)
            .current_dir(&daten_dir)
            .stdout(
              Stdio::from(log_datei)
            )
            .stderr(
              Stdio::from(fehler_log)
            )
            .creation_flags(
              CREATE_NO_WINDOW
            )
            .spawn()?;

        println!(
          "Backend gestartet. PID: {}",
          child.id()
        );

        let backend_process =
          app.state::<BackendProcess>();

        let mut backend =
          backend_process
            .child
            .lock()
            .expect(
              "Backend-Prozess konnte nicht gesperrt werden"
            );

        *backend = Some(child);
      }

      Ok(())
    })
    .build(
      tauri::generate_context!()
    )
    .expect(
      "Fehler beim Erstellen der Tauri-Anwendung"
    );

  app.run(
    |app_handle, event| {

      if matches!(
        event,
        RunEvent::ExitRequested { .. }
          | RunEvent::Exit
      ) {

        let backend_process =
          app_handle
            .state::<BackendProcess>();

        let mut backend =
          backend_process
            .child
            .lock()
            .expect(
              "Backend-Prozess konnte nicht gesperrt werden"
            );

        if let Some(mut child) =
          backend.take()
        {

          println!(
            "Backend wird beendet. PID: {}",
            child.id()
          );

          match child.kill() {
            Ok(_) => {
              println!(
                "Backend-Prozess wurde beendet."
              );
            }

            Err(fehler) => {
              eprintln!(
                "Backend konnte nicht beendet werden: {}",
                fehler
              );
            }
          }

          let _ = child.wait();
        }
      }
    }
  );
}
