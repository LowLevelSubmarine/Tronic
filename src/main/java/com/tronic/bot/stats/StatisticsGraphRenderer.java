package com.tronic.bot.stats;

import com.lowlevelsubmarine.subsconsole.Align;
import com.lowlevelsubmarine.subsconsole.graph_fragments.BarGraphFragment;
import com.lowlevelsubmarine.subsconsole.graph_fragments.StaticGraphFragment;
import com.lowlevelsubmarine.subsconsole.graph_fragments.VertexNameGraphFragment;
import com.lowlevelsubmarine.subsconsole.graph_fragments.VertexValueGraphFragment;
import com.lowlevelsubmarine.subsconsole.graphs.GraphComposer;

public class StatisticsGraphRenderer<T extends Number> extends GraphComposer<T> {

    @SuppressWarnings("unchecked")
    public StatisticsGraphRenderer(int barSize, double maxValue) {
        super(
                new StaticGraphFragment<>("|"),
                new VertexNameGraphFragment<>(Align.LEFT.createString("Name"), Align.LEFT),
                new VertexValueGraphFragment<>(Align.RIGHT.createString("Value"), Align.RIGHT),
                new StaticGraphFragment<>("|"),
                new BarGraphFragment<>(Align.CENTER.createString("Graph"), barSize, maxValue, '#',' ')
        );
    }

}
