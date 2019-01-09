package com.quicktutorialz.nio.endpoints.v2;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v2.ToDoDao;
import com.quicktutorialz.nio.daos.v2.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import io.reactivex.Observable;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by E076103 on 09-01-2019.
 */
public class ToDoEndPoints extends Endpoints{

  ToDoDao toDoDao = ToDoDaoImpl.getInstance();

  @Api(path = "/api/v2/create", method = "POST", consumes = "application/json", produces = "application/json", description = "")
  Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    Observable.just(request)
        .map(req -> (ToDoDto) getDataFromJsonBodyRequest(request, ToDoDto.class))
        //.map(toDoDto -> toDoDao.create(toDoDto))
        .flatMap(toDoDto -> toDoDao.create(toDoDto))
        .subscribe(output -> toJsonResponse(request, response, new ResponseDto(200, output)));
  };

  @Api(path = "/api/v2/read/{id}", method = "GET", produces = "application/json", description = "")
  Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    Observable.just(request)
        .map(req -> getPathVariables(request).get("id"))
        .flatMap(id -> toDoDao.read(id))
        .subscribe(output -> toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "to do not present")));
  };

  @Api(path = "/api/v2/read", method = "GET", produces = "application/json", description = "")
  Action readAllToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    Observable.just(request)
        .flatMap(r -> toDoDao.readAll())
        .subscribe(toDos -> toJsonResponse(request, response, new ResponseDto(200, toDos)));
  };

  @Api(path = "/api/v2/update", method = "POST", produces = "application/json", description = "")
  Action updateToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    Observable.just(request)
        .map(req -> (ToDo) getDataFromJsonBodyRequest(request, ToDo.class))
        .flatMap(toDo -> toDoDao.update(toDo))
        .subscribe(toDo -> toJsonResponse(request, response, new ResponseDto(200, toDo.isPresent() ? toDo.get() : "unknown to do")));
  };

  @Api(path = "/api/v2/delete/{id}", method = "DELETE", produces = "application/json", description = "")
  Action deleteToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    Observable.just(request)
        .map(req -> getPathVariables(request).get("id"))
        .flatMap(id -> toDoDao.delete(id))
        .subscribe(toDoStatus -> toJsonResponse(request, response, new ResponseDto(200, toDoStatus ? "to do deleted" : "unable to delete to do")));
  };


  public ToDoEndPoints() {
    setEndpoint("/api/v2/create", createToDo);
    setEndpoint("/api/v2/read/{id}", readToDo);
    setEndpoint("/api/v2/read", readAllToDo);
    setEndpoint("/api/v2/update", updateToDo);
    setEndpoint("/api/v2/delete/{id}", deleteToDo);
  }
}
