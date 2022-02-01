package netzplantechnik.ugur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NetzPlanController {
    public static List<Knot> knotList = new ArrayList<>();
    public static List<KnotInputForm> knotInputFormList = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/start")
    public String getStartPage(Model model) {
        return "home";
    }

    @GetMapping("input")
    public String getKnotInputForm(Model model) {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM netzplan");
        List<String> testData = new ArrayList<>();

        while (sqlRowSet.next()) {
            String testString = sqlRowSet.getInt("VORGANGSNUMMER") + " " + sqlRowSet.getString("VORGANGSBEZEICHNUNG ") +
             " " + sqlRowSet.getInt("DAUER") + " " + sqlRowSet.getInt("VORGAENGER1") + " " + sqlRowSet.getInt("VORGAENGER2") + " " + sqlRowSet.getInt("VORGAENGER3");
            testData.add(testString);
        }
        System.out.println(testData);

        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("knotInputFormList", knotInputFormList);

        return "knotInputForm";
    }

    @PostMapping("saveKnotInputForm")
    public String saveKnotInputForm(Model model, KnotInputForm knotInputForm) {
        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("knotInputFormList", knotInputFormList);

        /* String sql = "insert into TEST(ID, NAME) values (?, ?)";
        jdbcTemplate.update(sql, knotInputForm.getOperationNumber(), knotInputForm.getOperationDescription() ); */

        String sql = "insert into NETZPLAN(VORGANGSNUMMER, VORGANGSBEZEICHNUNG, DAUER, VORGAENGER1, VORGAENGER2, VORGAENGER3) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, knotInputForm.getOperationNumber(), knotInputForm.getOperationDescription(), knotInputForm.getDurationInMinutes(),
                knotInputForm.getPredecessorOneListIndex(), knotInputForm.getPredecessorTwoListIndex(), knotInputForm.getPredecessorThreeListIndex() );

                knotInputFormList.add(knotInputForm);

        return "knotInputForm";
    }

    @GetMapping("table")
    public String getNetPlanOutputTable(Model model) {
        knotList = convertKnotInputFormListToKnotList(knotInputFormList);
        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("knotList", knotList);


        return "outputTable";
    }

    public List<Knot> convertKnotInputFormListToKnotList(List<KnotInputForm> knotInputFormList) {

        List<Knot> result = new ArrayList<>();

        for (KnotInputForm inputForm : knotInputFormList) {
            Knot tempKnot = new Knot(inputForm.getOperationNumber(), inputForm.getOperationDescription(), inputForm.getDurationInMinutes());
            result.add(tempKnot);

            Integer predOneIndex = inputForm.getPredecessorOneListIndex();
            Integer predTwoIndex = inputForm.getPredecessorTwoListIndex();
            Integer predThreeIndex = inputForm.getPredecessorThreeListIndex();

            if (predOneIndex != null) {
                tempKnot.getPredecessorList().add(result.get(predOneIndex));
                result.get(predOneIndex).getSuccessorList().add(tempKnot);
            }

            if (predTwoIndex != null) {
                tempKnot.getPredecessorList().add(result.get(predTwoIndex));
                result.get(predTwoIndex).getSuccessorList().add(tempKnot);
            }

            if (predThreeIndex != null) {
                tempKnot.getPredecessorList().add(result.get(predThreeIndex));
                result.get(predThreeIndex).getSuccessorList().add(tempKnot);
            }
        }

        return result;
    }
}
