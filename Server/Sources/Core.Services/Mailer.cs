using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Mail;
using System.Text;
using System.Threading.Tasks;

namespace Core.Services
{
    public class Mailer : IMailer
    {

        public Mailer()
        {

        }

        async Task IMailer.SendAsync(string to, string body)
        {
            var message = new MailMessage("citylogia.team@gmail.com", to, "[Successful registration]", body);
           
            var client = new SmtpClient("smtp.gmail.com")
            {
                Port = 587,
                Credentials = new NetworkCredential("citylogia.team@gmail.com", "citylogia_team2021"),
                EnableSsl = true
            };

            await client.SendMailAsync(message);
        }
    }
}
