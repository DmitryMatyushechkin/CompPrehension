import { observer } from "mobx-react"
import React, { useEffect, useState } from "react"
import { ExerciseController } from "../controllers/exercise-controller";
import * as E from "fp-ts/lib/Either";
import { ExerciseStatisticsItem } from "../types/exercise-statistics";
import { Loader } from "../components/common/loader";

export const Statistics = () => {
    const [statistics, setStatistics] = useState([] as ExerciseStatisticsItem[]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        (async () => {
            const urlParams = new URLSearchParams(window.location.search);
            const exerciseId = urlParams.get('exerciseId');
            if (exerciseId === null || Number.isNaN(+exerciseId)) {
                throw new Error("Invalid exerciseId url param");
            }

            setIsLoading(true);
            const statistics = await ExerciseController.getExerciseStatistics(+exerciseId);
            if (E.isRight(statistics)) {
                setStatistics(statistics.right);
            }
            setIsLoading(false);
        })()
    }, []);


    return (
        <div>
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">AttemptId</th>
                        <th scope="col">QuestionsCount</th>
                        <th scope="col">TotalInteractionsCount</th>
                        <th scope="col">TotalInteractionsWithErrorsCount</th>
                        <th scope="col">AverageGrade</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        isLoading
                            ? <Loader />
                            : statistics.map(s => (
                                <tr>
                                    <th scope="row">{s.attemptId}</th>
                                    <td>{s.questionsCount}</td>
                                    <td>{s.totalInteractionsCount}</td>
                                    <td>{s.totalInteractionsWithErrorsCount}</td>
                                    <td>{s.averageGrade}</td>
                                </tr>
                            ))
                    }
                </tbody>
            </table>
        </div>
    );
};