package To_Do_App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ToDoAppApplication {

	public static void main(String[] args) {
		// Ejecuta la aplicación y obtén el contexto de Spring
		ApplicationContext context = SpringApplication.run(ToDoAppApplication.class, args);

		// Verifica si el contexto se cargó correctamente
		System.out.println("¡La aplicación To-Do App se ha iniciado correctamente!");

		// Opcional: Imprime los nombres de los beans cargados en el contexto
		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("Beans cargados en el contexto de Spring:");
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

}
