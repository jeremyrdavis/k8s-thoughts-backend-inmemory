package com.redhat.k8sthoughts;

import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Path("/api/thoughts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThoughtOfTheDayResource {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final String CURRENT_DATE = LocalDate.now().format(DATE_FORMATTER);

    // Use a LinkedHashMap to preserve insertion order
    protected static final Map<Integer, ThoughtOfTheDay> DEFAULT_THOUGHTS = new LinkedHashMap<>();
    static {
        DEFAULT_THOUGHTS.put(1, new ThoughtOfTheDay("Quarkus Loves You", "Quarkus", CURRENT_DATE));
        DEFAULT_THOUGHTS.put(2, new ThoughtOfTheDay("OpenShift Loves You", "OpenShift", CURRENT_DATE));
        DEFAULT_THOUGHTS.put(3, new ThoughtOfTheDay("Red Hat Loves You", "Red Hat", CURRENT_DATE));
        DEFAULT_THOUGHTS.put(4, new ThoughtOfTheDay("Kubernetes Loves You", "Kubernetes", CURRENT_DATE));
    }

    @GET
    public Collection<ThoughtOfTheDay> list() {
        return DEFAULT_THOUGHTS.values();
    }

    @GET
    @Path("/{id}")
    public ThoughtOfTheDay get(@PathParam("id") int id) {
        Log.debugf("Fetching thought with id %d", id);
        ThoughtOfTheDay thought = DEFAULT_THOUGHTS.get(id);
        if (thought != null) {
            Log.debugf("Thought with id %d found: %s", id, thought);
            return thought;
        } else {
            Log.debugf("Thought with id %d does not exist.", id);
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }

    @GET
    @Path("/random")
    public ThoughtOfTheDay getRandom() {
        List<ThoughtOfTheDay> values = new ArrayList<>(DEFAULT_THOUGHTS.values());
        Random random = new Random();
        ThoughtOfTheDay thought = values.get(random.nextInt(values.size()));
        Log.debugf("Random thought selected: %s", thought);
        return thought;
    }

    @POST
    @Transactional
    public Response create(ThoughtOfTheDay thought) {
        int nextId = DEFAULT_THOUGHTS.size() == 0 ? 1 : Collections.max(DEFAULT_THOUGHTS.keySet()) + 1;
        DEFAULT_THOUGHTS.put(nextId, thought);
        return Response.status(201).entity(thought).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ThoughtOfTheDay update(@PathParam("id") int id, ThoughtOfTheDay thought) {
        ThoughtOfTheDay existing = DEFAULT_THOUGHTS.get(id);
        if (existing != null) {
            existing.thought = thought.thought;
            existing.author = thought.author;
            existing.day = thought.day;
            DEFAULT_THOUGHTS.put(id, existing);
            return existing;
        } else {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") int id) {
        if (DEFAULT_THOUGHTS.remove(id) != null) {
            return Response.status(204).build();
        } else {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }
}