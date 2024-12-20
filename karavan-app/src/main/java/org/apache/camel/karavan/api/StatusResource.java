/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.karavan.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.camel.karavan.KaravanCache;
import org.apache.camel.karavan.model.CamelStatus;
import org.apache.camel.karavan.model.CamelStatusValue;
import org.apache.camel.karavan.model.DeploymentStatus;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/ui/status")
public class StatusResource {

    private static final Logger LOGGER = Logger.getLogger(StatusResource.class.getName());

    @Inject
    KaravanCache karavanCache;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deployment/{name}/{env}")
    public Response getDeploymentStatus(@PathParam("name") String name, @PathParam("env") String env) {
        DeploymentStatus status = karavanCache.getDeploymentStatus(name, env);
        if (status != null) {
            return Response.ok(status).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/camel")
    public List<CamelStatus> getCamelAllStatuses() {
        return karavanCache.getCamelAllStatuses();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/camel/{context}")
    public List<CamelStatus> getCamelContextStatusesByName(@PathParam("context") String context) {
        return karavanCache.getCamelStatusesByName(CamelStatusValue.Name.valueOf(context));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deployment")
    public Response deleteDeploymentStatuses() {
        karavanCache.deleteAllDeploymentsStatuses();
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/container")
    public Response deleteContainerStatuses() {
        karavanCache.deleteAllPodContainersStatuses();
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/camel")
    public Response deleteCamelStatuses() {
        karavanCache.deleteAllCamelStatuses();
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response deleteAllStatuses() {
        karavanCache.clearAllStatuses();
        return Response.ok().build();
    }
}