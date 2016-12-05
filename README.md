# SpringBootSpike

Implementación utilizando spring-boot del backend para el proyecto SETS. La arquitectura propuesta pretende cumplir los siguientes objetivos:

  - Simplicidad
  - Mantenibilidad
  - Lógica de negocio aislada del framework
  - Favorecer una buena estrategía de testing automático

# Estructura del proyecto

El proyecto sigue el tipico layout de una aplicación maven con spring boot:

- src
  - main
    - java
      - com.grupoasv
        - aspects
        - config
        - repository
        - controller
        - domain
        - usecase
        - SETSDemoApplication.java
  - test
    - java
      - com.grupoasv
        - usecase
        - ClientRestApiShould.java
- pom.xml

Breve explicacin de cada uno de los paquetes:

`aspects`: implementacion de conceptos transversales de la aplicación, en ese ejemplo la auditoria, posteriormente se explica en detalle.

`config`: ficheros de configuracin para spring-boot, la configuración se suele realizar en ficheros.java evitando los tediosos ficheros de configuracin xml tipicos de spring.

`controller`: controladores para servicios REST no auto-generados.

`repository`: Interfaces de los repositorios para acceso a datos.

`domain`: Clases de dominio.

`usecase`: Implementación de los distintos casos de uso de la aplicación, en este ejemplo el caso de uso "CreateServiceRequest".


# CRUD

No queremos perder mucho tiempo escribiendo código boiler-plate para generar CRUDS, utilizando spring data rest lo tenemos muy sencillo para lograr este objetivo, para generar un servicio REST con los métodos tipicos de CRUD solo es necesario escribir la clase entidad y un interfaz para el repositorio, ejemplo con la entidad cliente:

Entidad Cliente:
```java

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private Integer sip;

    private String name;
    private String phone;
}
```
Las anotaciones @Data @AllArgsConstructor @NoArgsConstructor pertenecen a [lombok](https://projectlombok.org/), nos ahorran escribir los getters/setters manualmente y nos generan un constructor vacío y otro con un parametro por cada campo de la clase de manera automática. No es por supuesto obligatorio el uso de estas anotaciones pero nos ahorra mucho código repetitivo.

Y el interfaz del repositorio

```java
@RepositoryRestResource
public interface ClientRepository extends CrudRepository<Client,Integer>{
}
```

La anotación @RepositoryRestResource hace que spring data rest publique el servicio sin necesidad de implementar nosotros nada más.

# Lógica de negocio del backend

Lo realmente interesante en cualquier proyecto, entendemos por lógica de negocio todas aquellas operaciones que vayan más alla de una simple operación CRUD. Por ejemplo en este spike tenemos que implementar la operación de creación de una solicitud de servicio que implica además de la creación de la propia solicitud de servicio crear el cliente primero si no existe y crear además la instancia del servicio.

Para la implementación de este tipo de operaciones se propone usar una arquitectura clean o hexagonal que nos permita cumplir con los objetivos de aislar la lógica de negocio del framework lo más posible y de permitirnos testear esta lógica de negocio de manera simple.

![Clean architecture](https://8thlight.com/blog/assets/posts/2012-08-13-the-clean-architecture/CleanArchitecture-8b00a9d7e2543fa9ca76b81b05066629.jpg)

Teniendo en cuenta el dibujo anterior vamos a ver como mapean las clases creadas para el caso de uso implementado en este spike según esta arqutiectura, de fuera hacía dentro:

  - UI: nuestra aplicación angular.
  - Controllers: [ServiceRequestController.java](https://github.com/cinfantesa/SpringBootSpike/blob/master/src/main/java/com/grupoasv/controller/ServiceRequestController.java)
  - Use cases: [CreateServiceRequest.java](https://github.com/cinfantesa/SpringBootSpike/blob/master/src/main/java/com/grupoasv/usecase/CreateServiceRequest.java)
  - Entities: Las clases del paquete com.grupoasv.domain

En esta arquitectura la clase CreateServiceRequest.java que representa el caso de uso para la creación de solicitudes de servicio es independiente del framework en gran médida lo que nos permitiría en el futuro reutilizar esta lógica de negocio o conservarla a pesar de que se cambié el framework utilizado.

Graciás a esta indepedencia podemos escribir un verdadero test unitario para esta caso de uso, que no depende en ningún caso de spring [CreateServiceRequestShould](https://github.com/cinfantesa/SpringBootSpike/blob/master/src/test/java/com/grupoasv/usecase/CreateServiceRequestShould.java) , este tipo de tests que nos permiten testear nuestra lógica de negocio con independencia de la infraestructura tienen grandes ventajas:

  - Se ejecutan muy rápido, miles de test en segundos.
  - Se ejecutan aisladamente, sin interdependencias, lo que permite ejecutarlos en cualquier orden o incluso
  en paralelo sin ningún problema.
  - No se tiene que modificar si cambiamos piezas de la infraestructura, por ejemplo si cambiamos X repositorio para que utilize mongo en lugar de SQL nuestros test de lógica de negocio no deberían verse afectados.

# Estrategía de testing

Una estrategía de testing automático efectiva debe tratar de cumplir con la piramide de testing:

![Testing pyramid](https://watirmelon.files.wordpress.com/2012/01/idealautomatedtestingpyramid.png)

Los test unitarios como los de los casos de uso que veiamos antes tienen un coste de ejecución muy bajo, estos test deberían componer nuestra principal herramienta de testing automático. Posteriormente podemos construir test de integración a nivel de API (por ejemplo, habría otras posibilidades) que nos aseguren que todos los elementos de la aplicación están bien conectados. Posteriormente el equipo de QA se encargará de los test que están mas arriba de la piramide que quedan fuera del scope de este spike.

Un ejemplo de test de integración a nivel de API:

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientRestApiShould {

    public static final int SIP = 1;
    public static final String CLIENT_NAME = "alfredo";
    public static final String CLIENT_PHONE = "12345";

    @Autowired ClientRepository clientRepository;
    @Value("${local.server.port}") int port;

    @Before
    public void setUp() {
        clientRepository.deleteAll();
        RestAssured.port = port;
    }

    @Test
    public void get_a_client() {
        clientRepository.save(new Client(SIP, CLIENT_NAME, CLIENT_PHONE));

        when()
          .get("/clients/{id}", SIP).
        then()
          .statusCode(HttpStatus.SC_OK)
          .body("name", equalTo(CLIENT_NAME))
          .body("phone", equalTo(CLIENT_PHONE));
    }
}
``` 

Este test al ejecutarse se encarga de levantar un servidor con spring boot y luego lanzar el test
contra el servicio levantado. Se ha utilizado la librería [RestAssured](https://github.com/rest-assured/rest-assured) para hacer el test lo más legible posible.

Otro elemento interesante en este test es la configuración del entorno para utilizar un "RANDOM_PORT", al no tener un puerto "hardcoded" spring buscará uno libre para lanzar la aplicación de modo que no se producirán errores si el puerto esta ocupado por algún otro proceso en el momento de lanzar el test. Este error muy habitual en entornos de IC con este tipo de tests (por ejemplo al ejecutar test de integración de varias ramas a la vez).

# Aspectos (Auditoria)

Como parte del spike había que implementar un mecanismo de auditoria para logar cuando un usuario accede a un servicio, la auditoría es un ejemplo tipico de [cross-cutting concern](https://en.wikipedia.org/wiki/Cross-cutting_concern), es decir, un concepto transversal a toda la aplicación. Estos elementos en spring se pueden implementar de una forma muy elegante haciendo uso del soporte de spring para programación orientada a aspectos (AOP), existen otras posibilidades de implementación, vamos a mostrar una posible solución con spring-aop para mostrar la versatilidad de esta solución.

El objetivo es implementar la auditoria de forma cohesiva dentro de un aspecto de auditoria en lugar de tener 
llamadas a auditar repartidos por distintos lugares del código, vemos el ejemplo:

```java
@Component
@Aspect
public class AuditingAspect {

    @AfterReturning("execution(* com.grupoasv.repository.ServiceRepository+.findOne(..))")
    public void logServiceAccess(JoinPoint joinPoint) {
        System.out.println("User Access to service with id: " + joinPoint.getArgs()[0]);
    }
}
```

Con la anotación @AfterReturning estamos indicando que queremos que el método logServiceAccess se ejecute después de aquellos métodos que coincidan con la expresión indicada. En este caso el método findOne del repositorio de servicios.

De esta manera se podrían encapsular en este aspecto todas las operaciones de auditoria de la app configurando
con que puntos de la aplicación queremos "enganchar" la auditoria. 
