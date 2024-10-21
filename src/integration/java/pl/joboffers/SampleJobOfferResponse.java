package pl.joboffers;

public interface SampleJobOfferResponse {
    default String bodyWithOneOfferJson() {
        return """
                [
                {
                    "title": "Junior Java Developer",
                    "company": "Fair Place Finance S.A.",
                    "salary": "6 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                }
                ]
                """.trim();
    }

    default String bodyWithTwoOffersJson() {
        return """
                [
                {
                    "title": "Junior Java Developer",
                    "company": "Fair Place Finance S.A.",
                    "salary": "6 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                },
                {
                    "title": "Młodszy Programista",
                    "company": "Reply Polska",
                    "salary": "5 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/mlodszy-programista-reply-polska-katowice-a08iptd7"
                }
                ]
                """.trim();
    }

    default String bodyWithThreeOffersJson() {
        return """
                [
                {
                    "title": "Junior Java Developer",
                    "company": "Fair Place Finance S.A.",
                    "salary": "6 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                },
                {
                    "title": "Młodszy Programista",
                    "company": "Reply Polska",
                    "salary": "5 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/mlodszy-programista-reply-polska-katowice-a08iptd7"
                },
                {
                    "title": "Junior Java Developer",
                    "company": "BlueSoft Sp. z o.o.",
                    "salary": "7 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                }
                ]
                """.trim();
    }

    default String bodyWithFourOffersJson() {
        return """
                [
                {
                    "title": "Junior Java Developer",
                    "company": "Fair Place Finance S.A.",
                    "salary": "6 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                },
                {
                    "title": "Młodszy Programista",
                    "company": "Reply Polska",
                    "salary": "5 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/mlodszy-programista-reply-polska-katowice-a08iptd7"
                },
                {
                    "title": "Junior Java Developer",
                    "company": "BlueSoft Sp. z o.o.",
                    "salary": "7 000 – 9 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                },
                {
                    "title": "Java (CMS) Developer",
                    "company": "Efigence SA",
                    "salary": "16 000 – 18 000 PLN",
                    "offerUrl": "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"
                }
                ]
                """.trim();
    }

    default String bodyWithZeroOffersJson() {
        return "[]";
    }
}
