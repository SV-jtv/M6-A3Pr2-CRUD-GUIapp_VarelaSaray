import java.sql.Connection


object ProductoService {

    fun insertarProducto(
        idFab: String,
        idProducto: String,
        descripcion: String,
        precio: Double,
        existencias: Int
    ): Boolean {
        val sql = "INSERT INTO productos (id_fab, id_producto, descripcion, precio, existencias) VALUES (?, ?, ?, ?, ?)"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, idFab, idProducto, descripcion, precio, existencias)
        }
        return false
    }

    fun actualizarPrecioProducto(idFab: String, idProducto: String, nuevoPrecio: Double): Boolean {
        val sql = "UPDATE productos SET precio = ? WHERE id_fab = ? AND id_producto = ?"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, nuevoPrecio, idFab, idProducto)
        }
        return false
    }

    fun eliminarProducto(idFab: String, idProducto: String): Boolean {
        val sql = "DELETE FROM productos WHERE id_fab = ? AND id_producto = ?"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, idFab, idProducto)
        }
        return false
    }

    fun listarProductos(): List<String> {
        val productos = mutableListOf<String>()
        val sql = "SELECT id_fab, id_producto, descripcion, precio, existencias FROM productos"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val producto =
                        "Fabricante: ${rs.getString("id_fab")}, ID Producto: ${rs.getString("id_producto")}, " +
                                "Descripción: ${rs.getString("descripcion")}, Precio: ${rs.getDouble("precio")}, " +
                                "Existencias: ${rs.getInt("existencias")}"
                    productos.add(producto)
                }
            }
        }
        return productos
    }

    private fun ejecutarActualizacion(conn: Connection, sql: String, vararg params: Any): Boolean {
        conn.prepareStatement(sql).use { stmt ->
            params.forEachIndexed { index, param ->
                stmt.setObject(index + 1, param)
            }
            return stmt.executeUpdate() > 0
        }
    }
}