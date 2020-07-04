package br.edu.ifsp.scl.todolistsdm.controller

import android.content.Context
import androidx.room.Room
import br.edu.ifsp.scl.todolistsdm.model.dao.TarefaDao
import br.edu.ifsp.scl.todolistsdm.model.database.ToDoListDatabase
import br.edu.ifsp.scl.todolistsdm.model.entity.Tarefa
import br.edu.ifsp.scl.todolistsdm.view.ToDoListInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToDoListPresenter(private val view: ToDoListInterface){

    /* Também conhece o modelo, mas por injeção de dependência, isso não é um problema */
    private val model: TarefaDao = Room.databaseBuilder(
        (view as Context),
        ToDoListDatabase::class.java,
        ToDoListDatabase.Constantes.DB_NAME
    ).build().getTarefaDao()

    fun buscarTarefas() {
        GlobalScope.launch {
            val listaTarefas = model.recuperarTarefas().value;
            listaTarefas?.let { view.setTarefas(it) }
        }
    }

    fun apagarTarefa(tarefa: Tarefa){
        GlobalScope.launch {
            model.removerTarefa(tarefa)
        }
    }

    fun apagarTarefas(vararg tarefa: Tarefa){
        GlobalScope.launch {
            model.removerTarefas(*tarefa)
        }
    }

    fun alterarTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            model.atualizarTarefa(tarefa)
            view.setRetorno(tarefa)
        }
    }

    fun salvarTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = model.inserirTarefa(tarefa)
            val tarefaRetorno = model.recuperaTarefa(id.toInt())

            //view.setRetorno(tarefaRetorno)
        }
    }
}