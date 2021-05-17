package co.simplon.springticketapi.controller;

import co.simplon.springticketapi.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/tickets")
@RestController
public class TicketController {

    private JdbcTemplate jdbcTemplate;

    public TicketController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Ticket> getAllTickets(@RequestParam boolean resolved) {
        // J'ai besoin de me connecter à la base
        // J'ai besoin de faire une requête SQL pour lister tous les tickets
        // J'ai besoin de mettre les résultats dans une liste
        List<Ticket> tickets = new ArrayList<>();
        String selectReq = "SELECT * FROM ticket where is_solved = ?";

        try (PreparedStatement statement = jdbcTemplate.getDataSource().getConnection().prepareStatement(selectReq)) {
            statement.setBoolean(1, resolved);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                tickets.add(new Ticket(set.getLong("id"),
                        set.getTimestamp("date").toLocalDateTime(),
                        set.getString("description"),
                        set.getLong("learner_idx"),
                        set.getBoolean("is_solved")));
            }

            set.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return tickets;
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        String selectReq = "SELECT * FROM ticket where id = ?";
        Ticket ticket = null;

        try (PreparedStatement statement = jdbcTemplate.getDataSource().getConnection().prepareStatement(selectReq)) {
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                ticket = new Ticket(set.getLong("id"),
                        set.getTimestamp("date").toLocalDateTime(),
                        set.getString("description"),
                        set.getLong("learner_idx"),
                        set.getBoolean("is_solved"));
            }

            set.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        String insertReq = "INSERT INTO ticket (date, description, learner_idx, is_solved) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = jdbcTemplate.getDataSource().getConnection().prepareStatement(insertReq, Statement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, Timestamp.valueOf(ticket.getDate()));
            statement.setString(2, ticket.getDescription());
            statement.setLong(3, ticket.getLearnerIdx());
            statement.setBoolean(4, ticket.isSolved());

            statement.execute();

            // Récupération de l'ID généré
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                ticket.setId(rs.getLong(1));
            }

            rs.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    @PutMapping("/{id}")
    public void setResolved(@PathVariable Long id) {
        String insertReq = "UPDATE ticket SET is_solved = ? where id = ?";

        try (PreparedStatement statement = jdbcTemplate.getDataSource().getConnection().prepareStatement(insertReq)) {
            statement.setBoolean(1, true);
            statement.setLong(2, id);

            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
