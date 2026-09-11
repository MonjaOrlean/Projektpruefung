use std::process::Command;
use tauri::Manager;

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_log::Builder::new().build())
        .setup(|app| {
            let backend_pfad = app
                .path()
                .resource_dir()?
                .join("resources")
                .join("VereinsplanerBackend")
                .join("VereinsplanerBackend.exe");

            println!("Backend-Pfad: {:?}", backend_pfad);

            #[cfg(target_os = "windows")]
            {
                use std::os::windows::process::CommandExt;

                const CREATE_NO_WINDOW: u32 = 0x08000000;

                Command::new(&backend_pfad)
                    .creation_flags(CREATE_NO_WINDOW)
                    .spawn()?;
            }

            Ok(())
        })
        .run(tauri::generate_context!())
        .expect("Fehler beim Starten der Tauri-Anwendung");
}
