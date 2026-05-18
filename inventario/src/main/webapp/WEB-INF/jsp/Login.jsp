<!DOCTYPE html>
<html class="light" lang="es">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Gata Shoes - Acceso al Sistema</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700;800&amp;family=Inter:wght@400;500;600&amp;display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>

    <script id="tailwind-config">
        tailwind.config = {
            darkMode: "class",
            theme: {
                extend: {
                    "colors": {
                        "on-secondary": "#ffffff",
                        "surface-container-low": "#f4f3f6",
                        "on-error-container": "#93000a",
                        "background": "#faf9fc",
                        "outline": "#7e7386",
                        "secondary-fixed": "#e1e0fb",
                        "surface-container-lowest": "#ffffff",
                        "primary": "#6500cd",
                        "on-primary": "#ffffff",
                        "primary-container": "#8218ff",
                        "on-surface-variant": "#4c4354",
                        "on-surface": "#1a1c1e"
                    }
                },
            },
        }
    </script>
</head>

<body class="bg-[#faf9fc] min-h-screen flex items-center justify-center p-6">

<script src="/js/data.js"></script>

<main class="w-full max-w-md">
    <div class="bg-surface-container-lowest rounded-3xl p-8 md:p-12 shadow-[0_20px_50px_-15px_rgba(101,0,205,0.15)]">

        <div class="mb-8 flex flex-col items-center">
            <div class="mb-4 bg-primary rounded-2xl p-3 shadow-lg shadow-primary/20">
                <span class="material-symbols-outlined text-white text-3xl" data-icon="settings_accessibility">settings_accessibility</span>
            </div>
            <h1 class="text-2xl font-black italic tracking-tighter text-on-surface">Gata Shoes</h1>
        </div>

        <div class="mb-8 bg-primary/5 border border-primary/20 rounded-xl p-4 flex flex-col gap-2">
                <span class="text-[10px] font-black uppercase tracking-widest text-primary flex items-center gap-1">
                    <span class="material-symbols-outlined text-sm">science</span> Modo Pruebas
                </span>
            <div class="flex flex-col gap-1 text-[11px] font-mono text-on-surface-variant">
                <p><strong class="text-on-surface">User:</strong> admin@gata.shoes</p>
                <p><strong class="text-on-surface">Pass:</strong> gata2024</p>
            </div>
        </div>

        <form id="login-form" action="/login" method="POST" class="w-full space-y-4">

            <input id="email" name="correo" type="email" placeholder="Correo electrónico" class="w-full bg-surface-container-low border-none rounded-xl py-4 px-4 focus:ring-2 focus:ring-primary transition-all text-sm" required/>

            <input id="password" name="password" type="password" placeholder="Contraseña" class="w-full bg-surface-container-low border-none rounded-xl py-4 px-4 focus:ring-2 focus:ring-primary transition-all text-sm" required/>

            <button type="submit" class="w-full bg-primary text-white font-bold py-4 rounded-xl hover:bg-primary/90 transition-all active:scale-[0.98]">
                Ingresar
            </button>
        </form>

        <div id="error-message" class="mt-4 text-center text-error text-xs font-bold hidden">
            Credenciales inválidas, intenta de nuevo.
        </div>
    </div>
</main>

</body>
</html>