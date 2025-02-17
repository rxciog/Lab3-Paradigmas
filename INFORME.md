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

Para leer tal archivo se puede remplazar en App.js la línea 134 por         
``` 
JavaRDD<String> articles = spark.read().textFile("./src/main/resources/wiki_dump_parcial.txt").javaRDD(); 
```

Cores por worker: 1

Memoria por worker: 2G

### 1 worker
 - Tiempo total: ~2.5 min 

### 2 workers
 - Tiempo total: ~2.0 min

### 4 workers:
 - Tiempo total: ~3.3min

Podemos observar que con un único worker, el tiempo de ejecución fue de aproximadamente 2.5 minutos, mientras que con dos workers se obtuvo la mejor mejora en rendimiento, reduciendo el tiempo a unos 2.0 minutos. Sin embargo, al incrementar a cuatro workers, se nota un aumento inesperado del tiempo de ejecución a ~3.3 minutos. 
Esto sugiere que, en este caso, el distribuir tareas en más nodos no siempre conduce a una mejora en el rendimiento, posiblemente debido al overhead en la gestión de más trabajadores.

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


# 4.Puntos a mejorar
Se debe resaltar que la implementación del uso de Spark es, en algunos casos, no óptima.  
Por ejemplo, en la heurística **CoreNLP**, el *Pipeline* se construye en cada tarea, lo cual es muy costoso.  
Del mismo modo, en la heurística **+capital**, la lista de *stopwords* se lee en cada tarea para filtar los candidatos a entidades nombradas.
Esto impacta la performance de la aplicación, sobre todo al utilizar alguna de esas dos heurísticas de extracción.

Una forma de mejorar esto es modificar el código para que el *driver* gestione estas actividades y luego comparta los recursos con cada nodo.
