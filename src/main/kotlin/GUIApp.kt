import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.stage.Stage

class GUIApp : Application() {
    override fun start(primaryStage: Stage) {
        val tablaCombo = ComboBox<String>()
        tablaCombo.items.addAll("clientes", "productos")
        tablaCombo.value = "clientes"

        val operacionCombo = ComboBox<String>()
        operacionCombo.items.addAll("Insertar", "Actualizar", "Eliminar", "Listar")
        operacionCombo.value = "Listar"

        val outputArea = TextArea().apply {
            isEditable = false
            prefRowCount = 15
        }

        val inputFields = VBox(10.0).apply {
            padding = Insets(10.0)
        }

        fun actualizarFormulario() {
            inputFields.children.clear()
            when (tablaCombo.value to operacionCombo.value) {
                "clientes" to "Insertar" -> {
                    inputFields.children.addAll(
                        Label("num_clie"), TextField().apply { id = "num_clie" },
                        Label("empresa"), TextField().apply { id = "empresa" },
                        Label("rep_clie"), TextField().apply { id = "rep_clie" },
                        Label("limite_credito"), TextField().apply { id = "limite_credito" }
                    )
                }

                "clientes" to "Actualizar" -> {
                    inputFields.children.addAll(
                        Label("num_clie"), TextField().apply { id = "num_clie" },
                        Label("nuevo limite_credito"), TextField().apply { id = "limite_credito" }
                    )
                }

                "clientes" to "Eliminar" -> {
                    inputFields.children.addAll(
                        Label("num_clie"), TextField().apply { id = "num_clie" }
                    )
                }

                "productos" to "Insertar" -> {
                    inputFields.children.addAll(
                        Label("id_fab"), TextField().apply { id = "id_fab" },
                        Label("id_producto"), TextField().apply { id = "id_producto" },
                        Label("descripcion"), TextField().apply { id = "descripcion" },
                        Label("precio"), TextField().apply { id = "precio" },
                        Label("existencias"), TextField().apply { id = "existencias" }
                    )
                }

                "productos" to "Actualizar" -> {
                    inputFields.children.addAll(
                        Label("id_fab"), TextField().apply { id = "id_fab" },
                        Label("id_producto"), TextField().apply { id = "id_producto" },
                        Label("nuevo precio"), TextField().apply { id = "precio" }
                    )
                }

                "productos" to "Eliminar" -> {
                    inputFields.children.addAll(
                        Label("id_fab"), TextField().apply { id = "id_fab" },
                        Label("id_producto"), TextField().apply { id = "id_producto" }
                    )
                }

                else -> {
                    // Listar no necesita campos
                }
            }
        }

        tablaCombo.setOnAction { actualizarFormulario() }
        operacionCombo.setOnAction { actualizarFormulario() }
        actualizarFormulario()

        val ejecutarBtn = Button("Ejecutar").apply {
            setOnAction {
                val inputs = inputFields.children.filterIsInstance<TextField>().associate { it.id to it.text.trim() }
                val tabla = tablaCombo.value
                val operacion = operacionCombo.value

                val resultado = when (tabla to operacion) {
                    "clientes" to "Insertar" -> ClienteService.insertarCliente(
                        inputs["num_clie"]!!.toInt(),
                        inputs["empresa"]!!,
                        inputs["rep_clie"]!!.toInt(),
                        inputs["limite_credito"]!!.toDouble()
                    ).toMensaje()

                    "clientes" to "Actualizar" -> ClienteService.actualizarLimiteCredito(
                        inputs["num_clie"]!!.toInt(),
                        inputs["limite_credito"]!!.toDouble()
                    ).toMensaje()

                    "clientes" to "Eliminar" -> ClienteService.eliminarCliente(
                        inputs["num_clie"]!!.toInt()
                    ).toMensaje()

                    "clientes" to "Listar" -> ClienteService.listarClientes().joinToString("\n")

                    "productos" to "Insertar" -> ProductoService.insertarProducto(
                        inputs["id_fab"]!!,
                        inputs["id_producto"]!!,
                        inputs["descripcion"]!!,
                        inputs["precio"]!!.toDouble(),
                        inputs["existencias"]!!.toInt()
                    ).toMensaje()

                    "productos" to "Actualizar" -> ProductoService.actualizarPrecioProducto(
                        inputs["id_fab"]!!,
                        inputs["id_producto"]!!,
                        inputs["precio"]!!.toDouble()
                    ).toMensaje()

                    "productos" to "Eliminar" -> ProductoService.eliminarProducto(
                        inputs["id_fab"]!!,
                        inputs["id_producto"]!!
                    ).toMensaje()

                    "productos" to "Listar" -> ProductoService.listarProductos().joinToString("\n")

                    else -> "Operación no válida"
                }

                outputArea.text = resultado
            }
        }

        val root = VBox(10.0, HBox(10.0, Label("Tabla:"), tablaCombo, Label("Operación:"), operacionCombo), inputFields, ejecutarBtn, outputArea)
        root.padding = Insets(15.0)
        val scene = Scene(root, 700.0, 600.0)

        primaryStage.title = "CRUD Clientes / Productos"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun Boolean.toMensaje() = if (this) "Operación exitosa" else "Ocurrió un error"
 /*   // Tabs para separar Clientes y Productos
    val tabClientes = Tab("Clientes", container)
    val tabProductos = Tab("Productos", productContainer)
    val tabPane = TabPane(tabClientes, tabProductos)

    val root = VBox(tabPane)
    stage.scene = Scene(root, 500.0, 500.0)
    stage.title = "Gestión CRUD - Clientes y Productos"
    stage.show()

// Inicializar formulario productos
    updateProductForm(prodOperationSelector.value)*/

}
