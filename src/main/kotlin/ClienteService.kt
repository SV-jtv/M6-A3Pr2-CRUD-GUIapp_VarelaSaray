import java.sql.Connection


object ClienteService {

    fun insertarCliente(numClie: Int, empresa: String, repClie: Int, limiteCredito: Double): Boolean {
        val sql = "INSERT INTO clientes (num_clie, empresa, rep_clie, limite_credito) VALUES (?, ?, ?, ?)"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, numClie, empresa, repClie, limiteCredito)
        }
        return false
    }

    fun actualizarLimiteCredito(numClie: Int, nuevoLimite: Double): Boolean {
        val sql = "UPDATE clientes SET limite_credito = ? WHERE num_clie = ?"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, nuevoLimite, numClie)
        }
        return false
    }

    fun eliminarCliente(numClie: Int): Boolean {
        val sql = "DELETE FROM clientes WHERE num_clie = ?"
        Database.getConnection()?.use { conn ->
            return ejecutarActualizacion(conn, sql, numClie)
        }
        return false
    }

    fun listarClientes(): List<String> {
        val clientes = mutableListOf<String>()
        val sql = "SELECT num_clie, empresa, rep_clie, limite_credito FROM clientes"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val cliente = "ID: ${rs.getInt("num_clie")}, Empresa: ${rs.getString("empresa")}, " +
                            "Rep: ${rs.getInt("rep_clie")}, Límite: ${rs.getDouble("limite_credito")}"
                    clientes.add(cliente)
                }
            }
        }
        return clientes
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
