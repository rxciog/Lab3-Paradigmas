---
title: Laboratorio de Programación Orientada a Objetos
author: Rocio Guadalupe Gómez
---
# Cambios significativos con respecto al Lab 2

Debido a que la API de HuggingFace tan solo permite un número limitado de consultas gratis, para realizar este laboratorio en donde se trabaja con big data, la clasificación de entidades nombradas se realiza consultando el diccionario provisto por la cátedra (dictionary.json).

# 1. Experiencia en el Tercer Laboratorio  
La parte más desafiante fue aprender a utilizar Spark, desde la configuración de un clúster en una máquina hasta la paralelización de tareas.  
Para ello, se recurrió a la documentación, así como a códigos de ejemplo y otros artículos disponibles en la red.  

También fue útil aprender a utilizar la interfaz gráfica para comprender qué estaba sucediendo en nuestro sistema en un momento dado. Esto facilitó la detección de errores en nuestras tareas gracias a los logs de los trabajadores.  



# 2. Desempeño con archivos grandes
Se evalúa el desempeño utilizando el archivo `wiki_dump_parcial.txt` provisto por la cátedra.
En este caso, se decidió realizar las mediciones usando la heurística *+capital*, ya que no da tantos falsos positivos como *capital*, es más eficiente que la heurística *CoreNLP* y permite identificar más entidades nombradas que *prefix*.

### 1 worker
 - Tiempo total: 17min

### 2 workers
 - Tiempo total: 8.6min

### 4 workers:
 - Tiempo total: 6.3min


# 3. Extras
Las siguientes tareas se realizan de forma distribuida (además de la extracción):  

- **Construcción de la Big Data**  
- **Clasificación de entidades nombradas**  

### Clasificación de Entidades Nombradas  

Dado que el uso de la API de Hugging Face es limitado, se decidió emplear la clasificación por diccionario. Sin embargo, fue necesario realizar algunos ajustes para que esto funcionara correctamente.  

En primer lugar, se notó que no era óptimo leer el diccionario en cada tarea. En su lugar, la mejor opción fue crearlo una sola vez en el *driver* y luego distribuirlo a cada nodo.  

Además, al analizar un programa de ejemplo para el conteo de palabras, se identificó que era posible utilizar herramientas de Spark para contar las menciones de cada entidad. Con base en esto, se realizaron los siguientes cambios:  

1. **Optimización del procesamiento**  
   - Al llamar al método de clasificación de entidades, se pasa el **RDD** junto con el punto de entrada de Spark.  
   
2. **Creación de la lista de entidades**  
   - Dentro del clasificador, se genera la lista `entitiesInDict`, que contiene las entidades nombradas disponibles en el diccionario.  
   - Se calcula la frecuencia de cada palabra en el **RDD**, obteniendo así el número de menciones por entidad.  
   - Se construye un nuevo **RDD** (`countedEntities`) con cada entidad y su número de menciones, lo que también permite eliminar duplicados.  

3. **Uso de variables de *broadcast***  
   - Se crea una variable de *broadcast* a partir de `entitiesInDict`.  
   - Para cada partición de `countedEntities`, se llama al método de clasificación, compartiendo la variable de *broadcast* para mejorar el rendimiento



  