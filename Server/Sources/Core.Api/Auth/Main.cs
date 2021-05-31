using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Auth.Models.Input;
using Core.Api.Auth.Models.Output;
using Core.Services;
using Core.Tools.Interfaces.Auth;
using Libraries.Auth;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Auth
{
    [ApiController]
    [Route("/api/Auth")]
    public class Main : ApiController
    {
        private readonly SqlContext context;
        private readonly IJwtManager jwtManager;
        private readonly IMailer mailer;

        public Main(SqlContext context, IJwtManager jwtManager, IMailer mailer)
        {
            this.context = context;
            this.jwtManager = jwtManager;
            this.mailer = mailer;
        }


        [HttpPost("Register")]
        public async Task<AuthenticateResponse> RegisterAsync([FromBody] RegisterParameters parameters)
        {
            var existed = this.context.Users.ToList().Any(u => u.Email == parameters.Email);
            if (existed)
            {
                throw new Exception("User already exists!");
            }

            var @new = parameters.Build();

            var users = this.context.Users;
            var user = await users.AddAsync(@new);
            await this.context.SaveChangesAsync();

            try
            {
                await this.mailer.SendAsync(parameters.Email, "Вы успешно зарегистрировались! Добро пожаловать в ситилогию!");
            }
            catch(Exception e)
            {
                Console.WriteLine(e);
            }

            var tokenPair = await jwtManager.GeneratePairAsync(user.Entity.Id);

            return new AuthenticateResponse(tokenPair);
        }

        [HttpPost("Email")]
        public async Task<AuthenticateResponse> LoginAsync([FromBody] LoginParameters parameters)
        {
            var user = this.context.Users.FirstOrDefault(u => u.Email == parameters.Email);

            var res = CheckUser(user, parameters.Password);

            if (!res)
            {
                throw new Exception("Invalid email or password!");
            }

            var tokenPair = await jwtManager.GeneratePairAsync(user.Id);

            return new AuthenticateResponse(tokenPair);
        }

        private bool CheckUser(User user, string password)
        {
            if (default == user)
            {
                return false;
            }

            if (!PasswordHandler.CheckPassword(password, user.Password))
            {
                return false;
            }

            return true;
        }
    }
}
