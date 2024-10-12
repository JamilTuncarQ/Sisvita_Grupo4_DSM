# Proyecto de Desarrollo de Sistemas Moviles
Se toma como base al programa Fer App que usa los datos de kaggle
Pasos para ejecutar el programa:
1. Descargar el repositorio
2. Abrir el proyecto en Android Studio
3. Ejecutar el programa
4. permitir el acceso a la camara para poder escanear los fotogramas de su rostro
5. mantener enfocado su camara por unos segundos(10 como minimo) para que pueda detectar su rostro
6. cierre el programa
7. en el device explorer de android studio, busque la carpeta data/data/com.vicksam.ferapp/databases
8. en esta carpeta encontraras el archivo estados.db en formato sqlite
9. descargue el archivo y abra con un programa que pueda leer archivos sqlite
10. en la tabla de estados encontrara los estados de animo que se detectaron en su rostro y su deteccion de porcentajes de ansiedad