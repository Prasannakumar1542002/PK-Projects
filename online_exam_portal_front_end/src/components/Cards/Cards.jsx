import React, { useEffect, useState } from "react";
import "./Cards.css";
import { cardsData } from "../../Data/Data";

import Card from "../Card/Card";
import img from "../../components/image/user.png"
import useStateRef from "react-usestateref";
import { log } from "util";
import Report from "../../page/Report";

const Cards = () => {
  var card = []
  var topic = []
  var topicName = []
  const [userAttemptAnswerMasterList, setUserAttemptAnswerMasterList] = useState();
  const [questions, setQuestions] = useState();
  const [userAttemptMasterMap, setUserAttemptMasterMap] = useState();
  const [exams, setExam] = useStateRef();
  const [topicList, setTopicList] = useStateRef();
  const [detail, setDetail] = useState(false);
  const[retrivedData,setRetrivedData]=useState()
  const colorArr = [
    "linear-gradient(-225deg, #7de2fc 0%, #b9b6e5 100%)",
    "linear-gradient(-225deg, #D4145A 0%, #FBB03B 100%)",
    "linear-gradient(-225deg, #009245 0%, #FCEE21 100%)",
    "linear-gradient(-225deg, #FF512F 0%, #DD2476 100%)",
    "linear-gradient(-225deg, #11998E 0%, #38EF7D 100%)",
    "linear-gradient(180deg, #662D8C  0%, #ED1E79 100%)"
  ];

  const getRandomColor = () => {
    return colorArr[Math.floor(Math.random() * colorArr.length)];
  };
  useEffect(() => {
    console.log("Cardfetch called...");
    fetchResult();

  }, []);
  // console.log("window location=",window.location.hostname);
  const url = "https://" + window.location.hostname + ":8443/OnlineExamPortal/control/fetch-user-report";
  const fetchResult = () => {
    fetch(url, {
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include'
    })
      .then((res) => res.json())
      .then((fetchedData) => {
        console.log("fetched...date", fetchedData);
        setRetrivedData(fetchedData);
        // setQuestions(fetchedData.questions);
        // setAnswers(fetchedData.userAttemptAnswerMasterList);
        // setScore(fetchedData.userAttemptMasterMap);
        // setExam(fetchedData.examList);
        // setTopicList(fetchedData.userAttemptTopicMasterList);
        fetchedData.examList.map((oneExam) => {
          console.log("fetchedexam", fetchedData.examWisePerformance[oneExam.examId].userAttemptMasterMap.score);
          fetchedData.examWisePerformance[oneExam.examId].userAttemptTopicMasterList.map((oneTopic) => {
            topic.push(Math.round(Number(oneTopic.userTopicPercentage)))
            // topicName.push(oneTopic.topicId)
          })
          setTopicList(topic);
          var topicList = fetchedData.examWisePerformance[oneExam.examId].TopicNameList;
          var info = {
            title: oneExam.examId,
            color: {
              backGround: getRandomColor(),
              boxShadow: "0px 10px 20px 0px #e0c6f5",
            },
            barValue: Math.round((Number(fetchedData.examWisePerformance[oneExam.examId].userAttemptMasterMap.score) / Number(oneExam.noOfQuestions)) * 100),
            value: oneExam.examName,
            series: [
              {
                name: "TopicPercentages",
                data: topic,
              },
            ],
            TopicNameList: topicList,
            examId:oneExam.examId
          }
          topic = [];
          card.push(info);

        })
        setExam(card);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
      });
  }
  // card=exams;
  console.log("outCard-", exams);

  const viewDetails=(examId)=>{
    setDetail(true);
    setQuestions(retrivedData.examWisePerformance[examId].questions);
    setUserAttemptAnswerMasterList(retrivedData.examWisePerformance[examId].userAttemptAnswerMasterList);
    setUserAttemptMasterMap(retrivedData.examWisePerformance[examId].userAttemptMasterMap);
  }
  const hideDetails=()=>{
    setDetail(false);
  }

  const cards = {
    title: "Sales",
    color: {
      backGround: "linear-gradient(180deg, #bb67ff 0%, #c484f3 100%)",
      boxShadow: "0px 10px 20px 0px #e0c6f5",
    },
    barValue: 50,
    value: "25,970",
    // png: UilUsdSquare,
    series: [
      {
        name: "Sales",
        data: [31, 40, 28, 51, 42, 109, 100],
      },
    ],
  }
  return (
    <div className="Cards">
      {detail?<Report questions={questions} userAttemptAnswerMasterList={userAttemptAnswerMasterList} userAttemptMasterMap={userAttemptMasterMap} hideDetails={hideDetails}/>:(exams ? exams.map((card, id) => {
        console.log("card=======>", card.TopicNameList, "======>", card.barValue,card.color);
        return (
          <div key={id}>
            <Card
              title={card.title}
              color={card.color}
              barValue={card.barValue}
              value={card.value}
              series={card.series}
              topic={card.TopicNameList}
              examId={card.examId}
              viewDetails={viewDetails}
            />
          </div>
        );
      }) : <p className="ml-5">No content available to view.</p>)}
    </div>
  );
};

export default Cards;
