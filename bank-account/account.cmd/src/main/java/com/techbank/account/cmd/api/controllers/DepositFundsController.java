package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/vs/depositFunds")
public class DepositFundsController {

    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String id,
                                                     @RequestBody DepositFundsCommand depositFundsCommand) {
        try {
            depositFundsCommand.setId(id);
            commandDispatcher.send(depositFundsCommand);
            return new ResponseEntity<>(new OpenAccountResponse("Deposit funds compoleted successfully", id), HttpStatus.OK);
        } catch (IllegalStateException | IllegalArgumentException exception) {
            logger.log(Level.WARNING, "illegal user operation");
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e1) {
            return new ResponseEntity<>(new BaseResponse(e1.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
