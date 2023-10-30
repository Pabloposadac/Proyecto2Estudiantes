import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.*;


class Main {
    static List<Estudiante> estudiante;

    public static void main(String[] args) throws IOException {
        cargarArchivo();
        aspirantesPorCarrera();
        HombresPorCarrera();
        MujeresPorCarrera();
        EstudianteConPuntajeMasAlto();
        EstudianteConPuntajeMasAltoPorCarrera();
        PromedioPuntajePorCarrera();


    }

    static void cargarArchivo() throws IOException {
        Pattern pattern = Pattern.compile(",");
        String filename = "student-scores.csv";

        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            estudiante = lines.skip(1).map(line -> {
                String[] arr = pattern.split(line);
                return new Estudiante((arr[0]), arr[1], arr[2], arr[4], arr[9], Integer.parseInt(arr[10]));
            }).collect(Collectors.toList());
            estudiante.forEach(System.out::println);
        }
    }

    // funcion para mostrar los aspirantes por carrera
    static void aspirantesPorCarrera() {
        System.out.printf("%nAspirantes por carrera:%n");
        Map<String, List<Estudiante>> aspirantesPorCarrera =
                estudiante.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCareer_aspiration));
        aspirantesPorCarrera.forEach(
                (career_aspiration, aspirantesEnCarrera) ->
                {
                    System.out.println(career_aspiration);
                    aspirantesEnCarrera.forEach(
                            aspirante -> System.out.printf(" %s%n", aspirante));
                }
        );
    }

    //funcion para mostrar la cantidad de hombres por carrera
    static void HombresPorCarrera() {
        System.out.printf("%nCantidad de hombres por carrera:%n");
        Map<String, Long> conteoHombresPorCarrera =
                estudiante.stream()
                        .filter(estudiante -> "male".equalsIgnoreCase(estudiante.getGender()))
                        .collect(Collectors.groupingBy(Estudiante::getCareer_aspiration,
                                TreeMap::new, Collectors.counting()));
        conteoHombresPorCarrera.forEach(
                (career_aspiration, conteo) -> System.out.printf(
                        "%s tiene %d hombre(s)%n", career_aspiration, conteo));

    }

    //funcion para mostrar la cantidad de mujeres por carrera

    static void MujeresPorCarrera() {
        System.out.printf("%nCantidad de mujeres por carrera:%n");
        Map<String, Long> conteoMujeresporCarrera =
                estudiante.stream()
                        .filter(estudiante1 -> "female".equalsIgnoreCase(estudiante1.getGender()))
                        .collect(Collectors.groupingBy(Estudiante::getCareer_aspiration,
                                TreeMap::new, Collectors.counting()));
        conteoMujeresporCarrera.forEach(
                (career_aspiration, conteo) -> System.out.printf(
                        "%s tiene %d mujer(es)%n", career_aspiration, conteo));
    }

//funcion para mostrar el estudiante con el puntaje mas alto

    static Predicate<Estudiante> puntajeMasAlto =
            e -> (e.getMath_score() >= 100);

    static void EstudianteConPuntajeMasAlto() {
        System.out.printf(
                "%nEstudiante con el puntaje mas alto:%n");
        estudiante.stream()
                .filter(puntajeMasAlto)
                .sorted(Comparator.comparing(Estudiante::getMath_score))
                .forEach(System.out::println);
    }

//funcion para mostrar el estudiante con el puntaje mas alto por carrera

    static void EstudianteConPuntajeMasAltoPorCarrera() {
        System.out.printf(
                "%nEstudiante con el puntaje mas alto por carrera:%n");
        Map<String, List<Estudiante>> aspirantesPorCarrera =
                estudiante.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCareer_aspiration));
        aspirantesPorCarrera.forEach(
                (career_aspiration, aspirantesEnCarrera) ->
                {
                    System.out.println(career_aspiration);
                    aspirantesEnCarrera.stream()
                            .filter(puntajeMasAlto)
                            .sorted(Comparator.comparing(Estudiante::getMath_score))
                            .forEach(System.out::println);
                }
        );
    }

//funcion para mostrar el promedio de puntaje por carrera

    static void PromedioPuntajePorCarrera() {
        System.out.printf(
                "%nPromedio de puntaje por carrera:%n");
        Map<String, List<Estudiante>> aspirantesPorCarrera =
                estudiante.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCareer_aspiration));
        aspirantesPorCarrera.forEach(
                (career_aspiration, aspirantesEnCarrera) ->
                {
                    System.out.print(career_aspiration+": ");
                    System.out.println(aspirantesEnCarrera.
                            stream().
                            mapToInt(Estudiante::getMath_score)
                            .average()
                            .getAsDouble());
                });
    }

}