package netzplantechnik.ugur;

import java.util.ArrayList;
import java.util.List;

public class Knot {

    private int operationNumber;
    private String operationDescription;
    private int durationInMinutes;
    private List<Knot> successorList = new ArrayList<>();
    private List<Knot> predecessorList = new ArrayList<>();

    @Override
    public String toString() {
        return "" +
                "" + operationNumber +
                '}';
    }

    // burdan 0 gelirse mesela newKnot = getPredecessorList().get(0) olur
    public int calculateFaz() {
        int result = 0;
        for (int i = 0; i < this.getPredecessorList().size(); i++) {
            // burdan 0 gelirse mesela newKnot = getPredecessorList().get(0) olur
            Knot newKnot = getPredecessorList().get(i);
            int newFez = newKnot.getFez();
            // buraya koydugumuz newfez aslinda kayit etmek icin konuldu sadece
            if (newFez > result) {
                result = newFez;
            }
        }
        return result;
    }
    public int calculateSez() {
        int result = Integer.MAX_VALUE;
        if (this.successorList.size() == 0) {
            return this.getFez();
        } else {
            for (int i = 0; i < getSuccessorList().size(); i++) {
                Knot successor = getSuccessorList().get(i);

                int successorSaz = successor.getSaz();

                if (successorSaz < result) {
                    result = successorSaz;
                }
            }
        }
        return result;
    }

    public int calculateTotalBuffer() {
        int totalBuffer = this.getSaz() - this.getFaz();
        return totalBuffer;
    }

    public int calculateFreeBuffer() {
        int freeBuffer;
        if (getSuccessorList().size() == 0) {
            return 0;
        }

        freeBuffer = getSuccessorList().get(0).getFaz() - this.getFez();
        return freeBuffer;
    }

    public List<Knot> calculateCriticalPath() {
        List<Knot> criticalPath = new ArrayList<>();
        Knot criticalKnot = this;

        if (successorList.isEmpty()) {
            criticalPath.add(criticalKnot);

            while (!criticalKnot.getPredecessorList().isEmpty())
                for (int i = 0; i < criticalKnot.getPredecessorList().size(); i++) {
                    if (criticalKnot.getPredecessorList().get(i).getFreeBuffer() == 0 && criticalKnot.getPredecessorList().get(i).getTotalBuffer() == 0) {
                        criticalKnot = criticalKnot.getPredecessorList().get(i);
                        criticalPath.add(criticalKnot);
                    }

                }
        }
        return criticalPath;
    }


    public Knot(int operationNumber, String operationDescription, int durationInMinutes, List<Knot> predecessorList) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.predecessorList = predecessorList;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, int faz, int fez, int saz,
                int sez) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;

    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, int faz, int fez, int saz,
                int sez, int totalBuffer, int freeBuffer) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;

    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, int faz, int fez, int saz,
                int sez, int totalBuffer, int freeBuffer, List<Knot> successorList, List<Knot> predecessorList) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.successorList = successorList;
        this.predecessorList = predecessorList;
    }

    public Knot() {
    }

    public int getFaz() {
        return calculateFaz();
    }

    public int getFez() {
        return this.getFaz() + this.durationInMinutes;
    }

    public int getSez() {
        return calculateSez();
    }

    public int getSaz() {
        return getSez() - this.durationInMinutes;
    }

    public int getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getTotalBuffer() {
        return calculateTotalBuffer();
    }

    public int getFreeBuffer() {
        return calculateFreeBuffer();
    }

    public List<Knot> getSuccessorList() {
        if (successorList == null) {
            successorList = new ArrayList<>();
        }
        return successorList;
    }

    public void setSuccessorList(List<Knot> successorList) {
        this.successorList = successorList;
    }

    public List<Knot> getPredecessorList() {
        if (predecessorList == null) {
            predecessorList = new ArrayList<>();
        }
        return predecessorList;
    }

    public void setPredecessorList(List<Knot> predecessorList) {
        this.predecessorList = predecessorList;
    }



}


