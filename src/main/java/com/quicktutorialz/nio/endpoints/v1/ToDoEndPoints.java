package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v1.ToDoDao;
import com.quicktutorialz.nio.daos.v1.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by E076103 on 09-01-2019.
 */
public class ToDoEndPoints extends Endpoints{

  ToDoDao toDoDao = ToDoDaoImpl.getInstance();

  @Api(path = "/api/v1/create", method = "POST", consumes = "application/json", produces = "application/json", description = "")
  Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    ToDoDto input = (ToDoDto) getDataFromJsonBodyRequest(request, ToDoDto.class);

    ToDo output = toDoDao.create(input);
    toJsonResponse(request, response, new ResponseDto(200, output));

  };

  @Api(path = "/api/v1/read/{id}", method = "GET", produces = "application/json", description = "")
  Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    String id = getPathVariables(request).get("id");
    Optional<ToDo> output = toDoDao.read(id);
    toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "to do not present"));
  };

  @Api(path = "/api/v1/read", method = "GET", produces = "application/json", description = "")
  Action readAllToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    List<ToDo> output = toDoDao.readAll();
    toJsonResponse(request, response, new ResponseDto(200, output));
  };

  @Api(path = "/api/v1/update", method = "POST", produces = "application/json", description = "")
  Action updateToDo = (HttpServletRequest request, HttpServletResponse response) -> {
    ToDo input = (ToDo) getDataFromJsonBodyRequest(request, ToDo.class);
    Optional<ToDo> toDo = toDoDao.update(input);
    toJsonResponse(request, response, new ResponseDto(200, toDo.isPresent() ? toDo.get() : "unknown to do"));
  };

  @Api(path = "/api/v1/delete/{id}", method = "DELETE", produces = "application/json", description = "")
  Action deleteToDo = (HttpServletRequest request, HttpServletResponse response) -> {
   String id = getPathVariables(request).get("id");
   toJsonResponse(request, response, new ResponseDto(200, toDoDao.delete(id) ? "to do deleted" : "unable to delete to do"));
  };

  public ToDoEndPoints() {
    setEndpoint("/api/v1/create", createToDo);
    setEndpoint("/api/v1/read/{id}", readToDo);
    setEndpoint("/api/v1/read", readAllToDo);
    setEndpoint("/api/v1/update", updateToDo);
    setEndpoint("/api/v1/delete/{id}", deleteToDo);
  }
}
