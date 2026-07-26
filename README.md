# DayShulkers

Plugin de Spigot/Paper que permite abrir cualquier Shulker Box directamente al hacer clic derecho, en vez de colocarla en el mundo. Al abrirla y cerrarla se envían mensajes, títulos y sonidos totalmente configurables (soporta degradados de color).

## Características

- Clic derecho con una shulker en la mano → se abre su inventario, **no se coloca**.
- Funciona con las 16 shulkers de color + la shulker sin teñir.
- Mensajes y títulos 100% configurables en `config.yml`.
- Soporta colores `&a`, hex `&#RRGGBB` y degradados letra por letra `&x&R&R&G&G&B&B`.
- Sonido configurable al abrir/cerrar.
- `/dayshulkers reload` — recarga la config sin reiniciar el server.
- `/dayshulkers help` — muestra toda la info del plugin.

## Requisitos

- Java 17+
- Servidor Spigot o Paper 1.20.x

## Cómo compilarlo

1. Clona/sube este proyecto a tu repo de GitHub.
2. En una máquina con Maven instalado (o con GitHub Actions/Codespaces):
