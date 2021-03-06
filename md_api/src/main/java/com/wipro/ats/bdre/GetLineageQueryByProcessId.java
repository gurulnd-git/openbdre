/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.ats.bdre;

import com.wipro.ats.bdre.exception.MetadataException;
import com.wipro.ats.bdre.md.api.base.MetadataAPIBase;
import com.wipro.ats.bdre.md.dao.LineageQueryDAO;
import com.wipro.ats.bdre.md.dao.jpa.LineageQuery;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by AshutoshRai on 1/19/16.
 */
public class GetLineageQueryByProcessId extends MetadataAPIBase {

    private static final Logger LOGGER = Logger.getLogger(GetLineageQueryByProcessId.class);

    @Autowired
    private LineageQueryDAO lineageQueryDAO;

    private static final String[][] PARAMS_STRUCTURE = {
            {"pid", "sub-process-id", " Process id whose lineage query to be extracted"},
    };

    public GetLineageQueryByProcessId() {
        /* Hibernate Auto-Wire */
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }


    @Override
    public List<LineageQuery> execute(String[] params) {
        try {
            CommandLine commandLine = getCommandLine(params, PARAMS_STRUCTURE);
            String pid = commandLine.getOptionValue("sub-process-id");
            LOGGER.debug("Sub-Process id  is " + pid);
            if("EMPTY".equals(pid)){
                return lineageQueryDAO.listAll();
            }
            return lineageQueryDAO.getLastInstanceExecLists(Integer.parseInt(pid));
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            throw new MetadataException(e);
        }
    }
}
