package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.util.List;

public record PlotSeriesResource(Long plotId, String plotName, List<SeriesPointResource> points) {
}
